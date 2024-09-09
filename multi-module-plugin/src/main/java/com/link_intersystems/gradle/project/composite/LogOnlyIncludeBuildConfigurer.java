package com.link_intersystems.gradle.project.composite;

import org.slf4j.Logger;

public class LogOnlyIncludeBuildConfigurer implements IncludeBuildConfigurer {

    private final Logger logger;

    public LogOnlyIncludeBuildConfigurer(Logger logger) {
        this.logger = logger;
    }

    public void configure(IncludeBuildPath includeBuildPath) {
        String value = includeBuildPath.getValue();
        configure(value);
    }


    public void configure(String includeBuildPath) {
        logger.info("Debug mode: includeBuild(\"{}\")", includeBuildPath);
    }
}