package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MultiModulePlugin implements Plugin<Settings> {

    private Logger logger = LoggerFactory.getLogger(MultiModulePlugin.class);

    @Override
    public void apply(Settings settings) {
        File rootDir = settings.getRootDir();

        List<String> filterPaths = getFilterPaths(settings);
        logger.debug("Filter paths {} ", filterPaths);

        IncludesCollector includesCollector = new IncludesCollector(rootDir.toPath(), filterPaths);
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
    private List<String> getFilterPaths(Settings settings) {
        List<String> filterPaths = new ArrayList<>();
        filterPaths.add("buildSrc");
        IncludeBuildsSource includeBuildsSource = new PluginManagementIncludeBuildSource(settings);
        filterPaths.addAll(includeBuildsSource.getIncludedBuilds());
        return filterPaths;
    }


}
