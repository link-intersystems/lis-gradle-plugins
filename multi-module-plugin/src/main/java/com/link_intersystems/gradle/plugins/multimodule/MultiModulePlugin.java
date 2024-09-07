package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.ExtensionContainer;
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
    public void apply(Settings s) {

        ExtensionContainer extensions = s.getExtensions();
        ConfigValues settingsConfigValues = extensions.create(MultiModuleConfig.class, "multiModule", MultiModuleConfig.class);

        ConfigValuesChain multiModuleConfigValuesChain = new ConfigValuesChain(
                settingsConfigValues,
                new GradlePropertyConfigValues(s),
                new SystemPropertyConfigValues(s),
                new EnvironmentConfigValues(s),
                new DefaultConfigValues()
        );

        s.getGradle().settingsEvaluated(settings -> {
            applyPlugin(settings, multiModuleConfigValuesChain);
        });

    }

    private void applyPlugin(Settings settings, ConfigValuesChain multiModuleConfigValuesChain) {
        File rootDir = settings.getRootDir();

        IncludesCollector includesCollector = new IncludesCollector(rootDir.toPath());
        Predicate<Path> excludePathsPredicate = new DefaultExcludePaths(settings);
        Predicate<Path> excludePaths = getExcludePaths(excludePathsPredicate, multiModuleConfigValuesChain);
        includesCollector.setExcludePaths(excludePaths);

        try {
            includesCollector.collect();

            IncludeBuildConfigurer includeBuildConfigurer = new IncludeBuildConfigurer(settings, logger, multiModuleConfigValuesChain);
            includesCollector.getIncludeBuildPaths().forEach(includeBuildConfigurer::configure);

            IncludeProjectConfigurer includeProjectConfigurer = new IncludeProjectConfigurer(settings, logger, multiModuleConfigValuesChain);
            includesCollector.getIncludePaths().forEach(includeProjectConfigurer::configure);
        } catch (IOException e) {
            throw new RuntimeException("Unable to apply plugin 'com.link-intersystems.gradle.multi-module'", e);
        }
    }

    @NotNull
    private Predicate<Path> getExcludePaths(Predicate<Path> excludePathsPredicate, ConfigValues multiModuleConfig) {

        if (multiModuleConfig.getOmitDefaultExcludes()) {
            excludePathsPredicate = p -> false;
        }

        List<String> excludePaths = multiModuleConfig.getExcludedPaths();
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
