package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

class IncludeBuildConfigurer {

    private Settings settings;
    private final Logger logger;

    public IncludeBuildConfigurer(Settings settings, Logger logger) {
        this.settings = settings;
        this.logger = logger;
    }

    public void configure(String includeBuildPath) {
        logger.info("Adding includeBuild(\"{}\")", includeBuildPath);
        settings.includeBuild(includeBuildPath);
    }
}