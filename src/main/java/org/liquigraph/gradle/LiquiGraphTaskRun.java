package org.liquigraph.gradle;


import org.liquigraph.core.configuration.ConfigurationBuilder;

public class LiquiGraphTaskRun extends LiquiGraphTask{
    @Override
    protected ConfigurationBuilder withExecutionMode(ConfigurationBuilder configurationBuilder) {
        return configurationBuilder.withRunMode();
    }
}
