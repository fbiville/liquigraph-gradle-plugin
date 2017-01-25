package org.liquigraph.gradle;


import org.liquigraph.core.configuration.ConfigurationBuilder;

public class LiquigraphTaskRun extends LiquigraphTask {
    @Override
    protected ConfigurationBuilder withExecutionMode(ConfigurationBuilder configurationBuilder) {
        return configurationBuilder.withRunMode();
    }
}
