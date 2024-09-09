package com.link_intersystems.gradle.project.sub;

import org.slf4j.Logger;

public class LogOnlyIncludeProjectConfigurer implements IncludeProjectConfigurer {

    private final Logger logger;

    public LogOnlyIncludeProjectConfigurer(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void configure(IncludePath includePath) {
        logger.info("DryRun: Adding includeBuild(\"{}\")", includePath);
    }

}