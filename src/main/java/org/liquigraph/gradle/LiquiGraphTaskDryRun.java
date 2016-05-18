package org.liquigraph.gradle;


import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.liquigraph.core.configuration.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LiquiGraphTaskDryRun extends LiquiGraphTask{


    File destinationDir;

    @OutputDirectory
    @Optional
    public File getDestinationDir() {
        return destinationDir;
    }

    public void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir;
    }

    @Override
    protected ConfigurationBuilder withExecutionMode(ConfigurationBuilder configurationBuilder) {
        File file = getDestinationDir();
        Path path = null;
        if (file != null) {
            path = file.toPath();
        } else {
            path = getProject().getBuildDir().toPath().resolve("liquigraph");
            try {
                path = Files.createDirectory(path);
            } catch (IOException e) {
                getLogger().error("Can't create dry run result directory", e);
            }
        }

        getLogger().info("Dry run result dumped in {} directory", path);
        return configurationBuilder.withDryRunMode(path);
    }
}

