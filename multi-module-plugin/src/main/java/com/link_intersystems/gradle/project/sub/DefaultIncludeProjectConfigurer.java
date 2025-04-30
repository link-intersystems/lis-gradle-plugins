package com.link_intersystems.gradle.project.sub;

import com.link_intersystems.gradle.project.plugin.ConfigValues;
import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

import java.io.File;

public class DefaultIncludeProjectConfigurer extends AbstractIncludeProjectConfigurer {

    public DefaultIncludeProjectConfigurer(Settings settings, ConfigValues configValues, Logger logger) {
        super(settings, configValues, logger);
    }

    @Override
    protected void doConfigure(Settings settings, String projectName, File includeFile) {
        settings.include(projectName);
        settings.project(":" + projectName).setProjectDir(includeFile);
    }

}