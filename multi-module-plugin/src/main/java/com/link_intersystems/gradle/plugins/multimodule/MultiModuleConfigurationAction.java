package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.Action;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

class MultiModuleConfigurationAction implements Action<Settings> {

    private final ConfigValues configValues;
    private Logger logger = LoggerFactory.getLogger(MultiModuleConfigurationAction.class);

    MultiModuleConfigurationAction(ConfigValues configValues) {
        this.configValues = requireNonNull(configValues);
    }

    @Override
    public void execute(Settings settings) {
        File rootDir = settings.getRootDir();

        IncludesCollector includesCollector = new IncludesCollector(rootDir.toPath());
        Predicate<Path> excludePathsPredicate = new DefaultExcludePaths(settings);
        Predicate<Path> excludePaths = getExcludePaths(excludePathsPredicate);
        includesCollector.setExcludePaths(excludePaths);

        try {
            includesCollector.collect();

            IncludeBuildConfigurer includeBuildConfigurer = new IncludeBuildConfigurer(settings, logger, configValues);
            includesCollector.getIncludeBuildPaths().forEach(includeBuildConfigurer::configure);

            IncludeProjectConfigurer includeProjectConfigurer = new IncludeProjectConfigurer(settings, logger, configValues);
            includesCollector.getIncludePaths().forEach(includeProjectConfigurer::configure);
        } catch (IOException e) {
            throw new RuntimeException("Unable to apply plugin 'com.link-intersystems.gradle.multi-module'", e);
        }
    }

    @NotNull
    private Predicate<Path> getExcludePaths(Predicate<Path> excludePathsPredicate) {

        if (configValues.getOmitDefaultExcludes()) {
            excludePathsPredicate = p -> false;
        }

        List<String> excludePaths = configValues.getExcludedPaths();
        if (!excludePaths.isEmpty()) {
            PropertiesExcludePathPredicate propertiesExcludePathPredicate = new PropertiesExcludePathPredicate(excludePaths);
            propertiesExcludePathPredicate.getAppliedPaths().forEach(path -> {
                logger.debug("com.link-intersystems.gradle.multi-module exclude path '{}' added.", path);
            });
            propertiesExcludePathPredicate.getIgnoredPaths().forEach(path -> {
                logger.warn("com.link-intersystems.gradle.multi-module.exclude-paths contains an invalid path that will be ignored: '{}'", path);
            });

            excludePathsPredicate = excludePathsPredicate.or(propertiesExcludePathPredicate);
        }

        return excludePathsPredicate;
    }
}
