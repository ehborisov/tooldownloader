package io.qameta.tooldownloader;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class ExecutionResult {

    private int exitCode;
    private String output;
    private Exception error;

    public boolean isSuccess() {
        return exitCode == 0;
    }

    public void setOutput(final String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public void setExitCode(final int exitCode) {
        this.exitCode = exitCode;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public void setError(final Exception e) {
        this.error = e;
    }

    public Exception getError() {
        return error;
    }
}
