package io.qameta.tooldownloader;

import io.qameta.tooldownloader.archivers.ArchiveInstaller;
import io.qameta.tooldownloader.archivers.ArchiveType;
import io.qameta.tooldownloader.archivers.ZipInstaller;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class InstallationConfiguration {

    private Path toolHome;
    private ArchiveType archiveType;
    private URL downloadUrl;
    private Integer connectionTimeout = (int) SECONDS.toMillis(10);
    private Integer downloadTimeout = (int) SECONDS.toMillis(60);
    private String subdirectoryInArchive;
    private String expectedSha;

    private InstallationConfiguration(final Builder builder) {
        this.toolHome = builder.toolHome;
        this.archiveType = builder.archiveType;
        this.downloadUrl = builder.downloadUrl;
        this.connectionTimeout = builder.connectionTimeout;
        this.downloadTimeout = builder.downloadTimeout;
        this.subdirectoryInArchive = builder.subdirectoryInArchive;
        this.expectedSha = builder.sha256Sum;
    }

    public ArchiveType getArchiveType() {
        return archiveType;
    }

    public ArchiveInstaller getArchiver() throws InstallationException {
        switch (archiveType) {
            case ZIP:
                return new ZipInstaller();
            default:
                throw new InstallationException("Target archive type is not specified");
        }
    }

    public Path getToolHome() {
        return toolHome;
    }

    public URL getDownloadUrl() {
        return downloadUrl;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public Integer getDownloadTimeout() {
        return downloadTimeout;
    }

    public String getSubdirectoryInArchive() {
        return subdirectoryInArchive;
    }

    public String getExpectedSha256() {
        return expectedSha;
    }

    public Optional<ToolInstallation> download() {
        return new ToolDownloader(this).install();
    }

    public static class Builder {
        private Path toolHome;
        private ArchiveType archiveType;
        private URL downloadUrl;
        private Integer connectionTimeout = (int) SECONDS.toMillis(10);
        private Integer downloadTimeout = (int) SECONDS.toMillis(60);
        private String subdirectoryInArchive;
        private String sha256Sum;

        public Builder(final Path toolHome) {
            this.toolHome = toolHome;
        }

        public Builder withArchiveType(final ArchiveType archiveType) {
            this.archiveType = archiveType;
            return this;
        }

        public Builder withsubdirectoryInArchive(final String directoryName) {
            this.subdirectoryInArchive = directoryName;
            return this;
        }

        public Builder withConnectionTimeoutMs(final Integer timeout) {
            this.connectionTimeout = timeout;
            return this;
        }

        public Builder withDownloadTimeoutMs(final Integer timeout) {
            this.downloadTimeout = timeout;
            return this;
        }

        public Builder withDownloadUrl(final String downloadUrl) {
            try {
                this.downloadUrl = new URL(downloadUrl);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
            return this;
        }

        public Builder withExpectedSha256(final String sha256Sum) {
            this.sha256Sum = sha256Sum;
            return this;
        }

        public InstallationConfiguration build() {
            return new InstallationConfiguration(this);
        }
    }
}
