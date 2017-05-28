package io.qameta.tooldownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Optional;

import static java.nio.file.Files.createTempFile;
import static org.apache.commons.io.FileUtils.copyURLToFile;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.moveDirectory;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
class ToolDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToolDownloader.class);

    private final InstallationConfiguration configuration;

    ToolDownloader(final InstallationConfiguration configuration) {
        this.configuration = configuration;
    }

    Optional<ToolInstallation> install() {
        return download().map(distributionPath -> {
            try {
                LOGGER.info("Extracting file {} to {} ...", distributionPath, configuration.getToolHome());
                final File homeDir = configuration.getToolHome().toFile();
                if (homeDir.exists()) {
                    LOGGER.info("Directory {} is already in use, removing it..", homeDir);
                    deleteQuietly(homeDir);
                }
                if (Objects.nonNull(configuration.getSubdirectoryInArchive())) {
                    final Path extractionDir = distributionPath.getParent();
                    configuration.getArchiver().install(distributionPath, extractionDir);
                    moveDirectory(extractionDir.resolve(configuration.getSubdirectoryInArchive()).toFile(), homeDir);
                } else {
                    configuration.getArchiver().install(distributionPath, configuration.getToolHome());
                }
                return new ToolInstallation(homeDir);
            } catch (InstallationException | IOException e) {
                LOGGER.error("Failed to extract archive", e);
                return null;
            } finally {
                deleteQuietly(distributionPath.toFile());
            }
        });
    }

    private Optional<Path> download() {
        try {
            final Path downloadToFile = createTempFile("tool", configuration.getArchiveType().getExtension());
            LOGGER.info("Downloading from {} to {} ", configuration.getDownloadUrl(), downloadToFile);
            copyURLToFile(configuration.getDownloadUrl(), downloadToFile.toFile(), configuration.getConnectionTimeout(),
                    configuration.getDownloadTimeout());
            if (Objects.nonNull(configuration.getExpectedSha256())) {
                validateChecksum(downloadToFile);
            }
            return Optional.of(downloadToFile);
        } catch (IOException e) {
            LOGGER.error("Failed to download tool distribution", e);
        }
        return Optional.empty();
    }

    private void validateChecksum(final Path downloadedFile) throws IOException {
        try (InputStream is = Files.newInputStream(downloadedFile)) {
            MessageDigest md = getSha256Digest();
            final byte[] buffer = new byte[16384];
            try (DigestInputStream dis = new DigestInputStream(is, md)) {
                while (dis.read(buffer) != -1) ;
            }
            final String computedSum = new BigInteger(1, md.digest()).toString(16);
            if (!computedSum.equals(configuration.getExpectedSha256())) {
                throw new IOException("Downloaded tool distribution checksum mismatch");
            }
        }
    }

    private static MessageDigest getSha256Digest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Unable to get SHA-256 digest instance");
        }
    }
}
