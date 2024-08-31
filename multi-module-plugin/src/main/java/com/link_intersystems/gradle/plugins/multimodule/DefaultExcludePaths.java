package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

class DefaultExcludePaths extends PatternPathExclude {

    private final List<String> excludePaths;

    public DefaultExcludePaths(Settings settings) {
        excludePaths = new ArrayList<>();
        excludePaths.add("buildSrc");
        IncludeBuildsSource includeBuildsSource = new PluginManagementIncludeBuildSource(settings);
        excludePaths.addAll(includeBuildsSource.getIncludedBuilds());

        setExcludePaths(asList("**/src/test/**", "**/src/main/**"));
    }

    @Override
    public boolean test(Path path) {
        if (super.test(path)) {
            return true;
        }
        return excludePaths.contains(path.toString());
    }
}
