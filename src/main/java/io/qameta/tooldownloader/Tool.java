package io.qameta.tooldownloader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class Tool {

    final String name;
    final File executable;

    Tool(final String name, final File executable) {
        this.name = name;
        this.executable = executable;
    }

    public String getName() {
        return name;
    }

    public ExecutionResult execute(final String... args) {
        final LinkedList<String> argsList = new LinkedList<>(Arrays.asList(args));

        argsList.addFirst(executable.getAbsolutePath());
        if (SystemUtils.IS_OS_UNIX) {
            argsList.addFirst("bash");
            argsList.addFirst("/usr/bin/env");
        }

        final ExecutionResult result = new ExecutionResult();
        try {
            Process process = new ProcessBuilder()
                    .command(argsList)
                    .start();
            try (InputStream is = process.getInputStream()) {
                result.setExitCode(process.waitFor());
                result.setOutput(IOUtils.toString(is, Charset.defaultCharset()));
            }
        } catch (IOException | InterruptedException e) {
            result.setError(e);
        }
        return result;
    }
}
