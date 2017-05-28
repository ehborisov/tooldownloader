package io.qameta.tooldownloader;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class InstallationException extends Exception {

    public InstallationException(Exception e) {
        super(e);
    }

    public InstallationException(String m) {
        super(m);
    }
}
