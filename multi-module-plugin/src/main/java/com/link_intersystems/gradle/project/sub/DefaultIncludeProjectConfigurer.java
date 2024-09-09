package com.link_intersystems.gradle.project.sub;

import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

public class DefaultIncludeProjectConfigurer implements IncludeProjectConfigurer {

    private Settings settings;
    private final Logger logger;

    public DefaultIncludeProjectConfigurer(Settings settings, Logger logger) {
        this.settings = settings;
        this.logger = logger;
    }

    @Override
    public void configure(IncludePath includePath) {
        String includeProjectIdentifier = includePath.getValue();
        logger.info("Adding include(\"{}\")", includeProjectIdentifier);
        settings.include(includeProjectIdentifier);
    }

}