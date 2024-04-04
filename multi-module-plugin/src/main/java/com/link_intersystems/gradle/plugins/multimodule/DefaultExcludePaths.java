package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class DefaultExcludePaths implements Predicate<Path> {

    private final List<String> excludePaths;

    public DefaultExcludePaths(Settings settings) {
        excludePaths = new ArrayList<>();
        excludePaths.add("buildSrc");
        IncludeBuildsSource includeBuildsSource = new PluginManagementIncludeBuildSource(settings);
        excludePaths.addAll(includeBuildsSource.getIncludedBuilds());
    }

    @Override
    public boolean test(Path path) {
        return excludePaths.contains(path.toString());
    }
}
