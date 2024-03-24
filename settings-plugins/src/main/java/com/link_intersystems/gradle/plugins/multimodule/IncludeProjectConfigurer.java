package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

class IncludeProjectConfigurer {

    private Settings settings;
    private final Logger logger;

    public IncludeProjectConfigurer(Settings settings, Logger logger) {
        this.settings = settings;
        this.logger = logger;
    }

    public void configure(String includeProjectIdentifier) {
        logger.info("Adding include(\"{}\")", includeProjectIdentifier);
        settings.include(includeProjectIdentifier);
    }
}