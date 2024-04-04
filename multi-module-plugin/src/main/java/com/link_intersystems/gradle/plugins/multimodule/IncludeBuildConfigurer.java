package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

class IncludeBuildConfigurer {

    private final ConfigValues configValues;
    private Settings settings;
    private final Logger logger;

    public IncludeBuildConfigurer(Settings settings, Logger logger, ConfigValues configValues) {
        this.settings = settings;
        this.logger = logger;
        this.configValues = configValues;
    }

    public void configure(IncludeBuildPath includeBuildPath) {

        String value = includeBuildPath.getValue();
        configure(value);
    }


    public void configure(String includeBuildPath) {

        if (configValues.getDryRun()) {
            logger.info("Debug mode: includeBuild(\"{}\")", includeBuildPath);
            return;
        }

        logger.info("Adding includeBuild(\"{}\")", includeBuildPath);
        settings.includeBuild(includeBuildPath);
    }
}