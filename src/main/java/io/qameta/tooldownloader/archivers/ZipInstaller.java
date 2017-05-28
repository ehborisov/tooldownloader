package io.qameta.tooldownloader.archivers;

import io.qameta.tooldownloader.InstallationException;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.nio.file.Path;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class ZipInstaller implements ArchiveInstaller {

    @Override
    public void install(final Path archive, final Path extractionDir) throws InstallationException {
        try {
            new ZipFile(archive.toFile()).extractAll(extractionDir.toString());
        } catch (ZipException e) {
            throw new InstallationException(e);
        }
    }
}
