package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

public class MultiModulePlugin implements Plugin<Settings> {

    private Logger logger = LoggerFactory.getLogger(MultiModulePlugin.class);

    @Override
    public void apply(Settings settings) {
        File rootDir = settings.getRootDir();

        IncludesCollector includesCollector = new IncludesCollector(rootDir.toPath());
        Predicate<Path> excludePaths = getExcludePaths(settings);
        includesCollector.setExcludePaths(excludePaths);

        try {
            Files.walkFileTree(rootDir.toPath(), includesCollector);

            IncludeBuildConfigurer includeBuildConfigurer = new IncludeBuildConfigurer(settings, logger);
            includesCollector.getIncludeBuildPaths().forEach(includeBuildConfigurer::configure);

            IncludeProjectConfigurer includeProjectConfigurer = new IncludeProjectConfigurer(settings, logger);
            includesCollector.getIncludePaths().forEach(includeProjectConfigurer::configure);
        } catch (IOException e) {
            throw new RuntimeException("Unable to apply plugin 'com.link-intersystems.gradle.multi-module'", e);
        }
    }

    @NotNull
    private Predicate<Path> getExcludePaths(Settings settings) {
        MultiModulePluginProperties multiModulePluginProperties = new MultiModulePluginProperties(settings);

        Predicate<Path> excludePathsPredicate = new DefaultExcludePaths(settings);
        if(multiModulePluginProperties.isOmitDefaultExcludes()){
            excludePathsPredicate = p -> false;
        }

        List<String> excludePaths = multiModulePluginProperties.getExcludePaths();
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
