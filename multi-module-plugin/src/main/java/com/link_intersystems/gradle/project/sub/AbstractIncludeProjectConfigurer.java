package com.link_intersystems.gradle.project.sub;

import com.link_intersystems.gradle.project.plugin.ConfigValues;
import com.link_intersystems.gradle.project.plugin.ProjectNamingStrategy;
import org.gradle.api.initialization.ProjectDescriptor;
import org.gradle.api.initialization.Settings;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public abstract class AbstractIncludeProjectConfigurer implements IncludeProjectConfigurer {

    public static final String MODULE_PROPERTIES = "module.properties";
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
        File includeFile = includePath.getPath();
        Path includeFilePath = includeFile.toPath();
        String includeProjectPath = pathToNormalizedString(includeFilePath);

        String projectName = resolveProjectName(includeFilePath, includeProjectPath);

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

    private String resolveProjectName(Path includeFilePath, String includeProjectPath) {
        ProjectNamingStrategy projectNamingStrategy = configValues.getProjectNamingStrategy();
        String projectName = projectNamingStrategy.getProjectName(settings.getRootDir().toPath(), includeFilePath);

        Properties properties = getProperties(includeFilePath);

        if (properties.containsKey("name")) {
            String modulePropertiesProjectName = properties.getProperty("name");
            String msg = "Module '{}' name '{}', resolved by project naming strategy '{}', is overridden by module.properties name '{}'";
            logger.info(msg, includeProjectPath, projectName, projectNamingStrategy, modulePropertiesProjectName);
            projectName = modulePropertiesProjectName;
        }

        return projectName;
    }

    private Properties getProperties(Path includeFilePath) {
        Properties properties = new Properties();
        File rootDir = settings.getRootDir();
        Path rootPath = rootDir.toPath();
        Path absoluteIncludeFilePath = rootPath.resolve(includeFilePath);
        Path modulePropertiesFilepath = absoluteIncludeFilePath.resolve(MODULE_PROPERTIES);
        if (Files.exists(modulePropertiesFilepath)) {
            logger.info("Reading {} file at '{}'", MODULE_PROPERTIES, modulePropertiesFilepath);
            try (BufferedReader modulePropertiesReader = Files.newBufferedReader(modulePropertiesFilepath, StandardCharsets.UTF_8)) {
                properties.load(modulePropertiesReader);
            } catch (IOException e) {
                logger.error("Unable to read {} file at '{}'", MODULE_PROPERTIES, modulePropertiesFilepath, e);
            }

            logModulePropertiesRead(properties, modulePropertiesFilepath);
        } else {
            logger.debug("{} file at '{}' does not exist", MODULE_PROPERTIES, modulePropertiesFilepath);
        }
        return properties;
    }

    private void logModulePropertiesRead(Properties properties, Path modulePropertiesFilepath) {
        StringWriter stringWriter = new StringWriter();

        try (stringWriter) {
            properties.store(stringWriter, null);
        } catch (IOException e) {
            try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
                e.printStackTrace(printWriter);
            } catch (Exception ex) {
                logger.error("Unable to print stack trace of exception", ex);
            }
        }

        logger.info("Read {} file at '{}': {}", MODULE_PROPERTIES, modulePropertiesFilepath, "\n" + stringWriter);
    }

    protected abstract void doConfigure(Settings settings, String projectName, File includeFile);

    private String pathToNormalizedString(Path path) {
        FileSystem fileSystem = path.getFileSystem();
        String separator = fileSystem.getSeparator();
        return path.toString().replace(separator, "/");
    }

}
