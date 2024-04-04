package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

class IncludeProjectConfigurer {

    private Settings settings;
    private final Logger logger;
    private final ConfigValues configValues;

    public IncludeProjectConfigurer(Settings settings, Logger logger, ConfigValues configValues) {
        this.settings = settings;
        this.logger = logger;
        this.configValues = configValues;
    }

    public void configure(IncludePath includePath) {
        if (configValues.getDryRun()) {
            logger.info("DryRun: Adding includeBuild(\"{}\")", includePath);
            return;
        }

        String includeProjectIdentifier = includePath.getValue();
        logger.info("Adding include(\"{}\")", includeProjectIdentifier);
        settings.include(includeProjectIdentifier);
    }

}