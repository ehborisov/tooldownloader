package io.qameta.tooldownloader.archivers;

import io.qameta.tooldownloader.InstallationException;

import java.nio.file.Path;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public interface ArchiveInstaller {

    public void install(Path archivePath, Path extractionDirectory) throws InstallationException;
}
