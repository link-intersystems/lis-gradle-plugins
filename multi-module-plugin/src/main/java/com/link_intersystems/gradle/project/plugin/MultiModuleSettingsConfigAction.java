package com.link_intersystems.gradle.project.plugin;

import com.link_intersystems.gradle.project.DefaultExcludePaths;
import com.link_intersystems.gradle.project.PathMatcherPredicate;
import com.link_intersystems.gradle.project.composite.*;
import com.link_intersystems.gradle.project.sub.DefaultIncludeProjectConfigurer;
import com.link_intersystems.gradle.project.sub.IncludeProjectConfigurer;
import com.link_intersystems.gradle.project.sub.LogOnlyIncludeProjectConfigurer;
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

class MultiModuleSettingsConfigAction implements Action<Settings> {

    private final ConfigValues configValues;
    private Logger logger = LoggerFactory.getLogger(MultiModuleSettingsConfigAction.class);

    MultiModuleSettingsConfigAction(ConfigValues configValues) {
        this.configValues = requireNonNull(configValues);
    }

    @Override
    public void execute(Settings settings) {
        File rootDir = settings.getRootDir();

        IncludesCollector includesCollector = new IncludesCollector(rootDir.toPath());

        Predicate<Path> excludePaths = getExcludePaths(settings);
        includesCollector.setExcludePaths(excludePaths);

        try {
            includesCollector.collect();

            IncludeBuildConfigurer includeBuildConfigurer = getIncludeBuildConfigurer(settings);
            includesCollector.getIncludeBuildPaths().forEach(includeBuildConfigurer::configure);

            IncludeProjectConfigurer includeProjectConfigurer = getIncludeProjectConfigurer(settings);
            includesCollector.getIncludePaths().forEach(includeProjectConfigurer::configure);
        } catch (IOException e) {
            throw new RuntimeException("Unable to apply plugin 'com.link-intersystems.gradle.multi-module'", e);
        }
    }

    private IncludeProjectConfigurer getIncludeProjectConfigurer(Settings settings) {
        IncludeProjectConfigurer includeProjectConfigurer;

        if (configValues.getDryRun()) {
            includeProjectConfigurer = new LogOnlyIncludeProjectConfigurer(logger);
        } else {
            includeProjectConfigurer = new DefaultIncludeProjectConfigurer(settings, logger);
        }

        return includeProjectConfigurer;
    }

    private IncludeBuildConfigurer getIncludeBuildConfigurer(Settings settings) {
        IncludeBuildConfigurer includeBuildConfigurer;

        if (configValues.getDryRun()) {
            includeBuildConfigurer = new LogOnlyIncludeBuildConfigurer(logger);
        } else {
            includeBuildConfigurer = new DefaultIncludeBuildConfigurer(settings, logger);
        }

        return includeBuildConfigurer;
    }

    @NotNull
    private Predicate<Path> getExcludePaths(Settings settings) {

        if (configValues.getOmitDefaultExcludes()) {
            return p -> false;
        }

        Predicate<Path> excludePathsPredicate = new DefaultExcludePaths("buildSrc");

        IncludeBuildsSource includeBuildsSource = new PluginManagementIncludeBuildSource(settings);
        excludePathsPredicate = excludePathsPredicate.or(new DefaultExcludePaths(includeBuildsSource.getIncludedBuilds()));

        List<String> excludePaths = configValues.getExcludedPaths();
        if (!excludePaths.isEmpty()) {
            PathMatcherPredicate pathMatcherPredicate = new PathMatcherPredicate(excludePaths);
            pathMatcherPredicate.getAppliedPaths().forEach(path -> {
                logger.debug("com.link-intersystems.gradle.multi-module exclude path '{}' added.", path);
            });
            pathMatcherPredicate.getIgnoredPaths().forEach(path -> {
                logger.warn("com.link-intersystems.gradle.multi-module.exclude-paths contains an invalid path that will be ignored: '{}'", path);
            });

            excludePathsPredicate = excludePathsPredicate.or(pathMatcherPredicate);
        }

        return excludePathsPredicate;
    }
}
