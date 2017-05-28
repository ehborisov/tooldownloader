package io.qameta.tooldownloader;

import java.util.function.Function;

/**
 * @author Egor Borisov ehborisov@gmail.com
 */
public class ToolConfiguration {

    private String toolName;
    private String pathToExecutable;
    private String[] validationCommand;
    private Function<ExecutionResult, Boolean> validationCondition;

    private ToolConfiguration(final Builder builder) {
        this.toolName = builder.toolName;
        this.pathToExecutable = builder.pathToExecutable;
        this.validationCommand = builder.validationCommand;
        this.validationCondition = builder.validationCondition;
    }

    public String getPathToExecutable() {
        return pathToExecutable;
    }

    public String getToolName() {
        return toolName;
    }

    public String[] getValidationCommand() {
        return validationCommand;
    }

    public Function<ExecutionResult, Boolean> getValidationCondition() {
        return validationCondition;
    }

    public static class Builder {
        private String toolName;
        private String pathToExecutable;
        private String[] validationCommand;
        private Function<ExecutionResult, Boolean> validationCondition;

        public Builder withName(final String name) {
            this.toolName = name;
            return this;
        }

        public Builder withPathToExecutable(final String pathToExecutable) {
            this.pathToExecutable = pathToExecutable;
            return this;
        }

        public Builder withValidationCommand(final String... validationCommand) {
            this.validationCommand = validationCommand;
            return this;
        }

        public Builder withValidationCondition(final Function<ExecutionResult, Boolean> condition) {
            this.validationCondition = condition;
            return this;
        }

        public ToolConfiguration build() {
            return new ToolConfiguration(this);
        }
    }
}
