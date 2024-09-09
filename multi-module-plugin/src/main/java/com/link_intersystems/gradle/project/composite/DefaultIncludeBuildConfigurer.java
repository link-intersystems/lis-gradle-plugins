package com.link_intersystems.gradle.project.composite;

import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

public class DefaultIncludeBuildConfigurer implements IncludeBuildConfigurer {

    private Settings settings;
    private final Logger logger;

    public DefaultIncludeBuildConfigurer(Settings settings, Logger logger) {
        this.settings = settings;
        this.logger = logger;
    }

    @Override
    public void configure(IncludeBuildPath includeBuildPath) {

        String value = includeBuildPath.getValue();
        configure(value);
    }


    public void configure(String includeBuildPath) {
        logger.info("Adding includeBuild(\"{}\")", includeBuildPath);
        settings.includeBuild(includeBuildPath);
    }
}