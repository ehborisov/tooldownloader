package io.qameta.tooldownloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class ToolInstallation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ToolInstallation.class);

    private File installationDir;

    ToolInstallation(final File installationDir) {
        this.installationDir = installationDir;
    }

    public Tool getTool(final ToolConfiguration configuration) {
        final File executable = new File(installationDir, configuration.getPathToExecutable());
        final Tool tool = new Tool(configuration.getToolName(), executable);
        final boolean isValid = validateInstallation(executable, tool, configuration);
        return isValid ? tool : null;
    }

    private boolean validateInstallation(final File executable, final Tool tool,
                                         final ToolConfiguration configuration) {
        if (!executable.exists()) {
            LOGGER.error("No executable exists at " + executable.getAbsolutePath());
            return false;
        }

        if (!executable.canExecute() && !executable.setExecutable(true)) {
            LOGGER.error("Cannot set executable flag for executable " + executable.getAbsolutePath());
            return false;
        }

        final ExecutionResult result = tool.execute(configuration.getValidationCommand());
        return configuration.getValidationCondition().apply(result);
    }

    public static Optional<Tool> discover(final Path home, final ToolConfiguration toolConfig) {
        return Optional.ofNullable(new ToolInstallation(home.toFile()).getTool(toolConfig));
    }
}
