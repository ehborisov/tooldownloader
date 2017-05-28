package io.qameta.tooldownloader.archivers;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public enum ArchiveType {

    ZIP("zip"),
    RAR("rar"),
    TAR("tar"),
    TAR_GZ("tar.gz");

    private final String extension;

    ArchiveType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
