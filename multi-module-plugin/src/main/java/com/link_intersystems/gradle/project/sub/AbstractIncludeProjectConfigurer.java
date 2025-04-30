package com.link_intersystems.gradle.project.sub;

import com.link_intersystems.gradle.project.plugin.ConfigValues;
import com.link_intersystems.gradle.project.plugin.ProjectNamingStrategy;
import org.gradle.api.initialization.ProjectDescriptor;
import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import static java.util.Objects.*;

public abstract class AbstractIncludeProjectConfigurer implements IncludeProjectConfigurer {

    protected final Settings settings;
    protected final ConfigValues configValues;
    protected final Logger logger;

    public AbstractIncludeProjectConfigurer(Settings settings, ConfigValues configValues, Logger logger) {
        this.settings = requireNonNull(settings);
        this.configValues = requireNonNull(configValues);
        this.logger = requireNonNull(logger);
    }

    @Override
    public void configure(IncludePath includePath) {
        ProjectNamingStrategy projectNamingStrategy = configValues.getProjectNamingStrategy();
        File includeFile = includePath.getPath();
        Path includeFilePath = includeFile.toPath();
        String includeProjectPath = pathToNormalizedString(includeFilePath);

        String projectName = projectNamingStrategy.getProjectName(settings.getRootDir().toPath(), includeFilePath);
        ProjectDescriptor existingProject = settings.findProject(":" + projectName);
        if (existingProject != null) {
            Path existingProjectPath = settings.getRootDir().toPath().relativize(existingProject.getProjectDir().toPath());
            String msg = "Omitting '{}', because it resolves to an already included project name ':{}' => '{}'. " +
                    "Consider using a ProjectNamingStrategy that resolves unique names.";
            logger.warn(msg, includeProjectPath, projectName, pathToNormalizedString(existingProjectPath));
        } else {
            doConfigure(settings, projectName, includeFile);
            logger.info("Including '{}' as '{}'", includeProjectPath, ":" + projectName);
        }
    }

    protected abstract void doConfigure(Settings settings, String projectName, File includeFile);

    private String pathToNormalizedString(Path path) {
        FileSystem fileSystem = path.getFileSystem();
        String separator = fileSystem.getSeparator();
        return path.toString().replace(separator, "/");
    }

}
