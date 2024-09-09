package com.link_intersystems.gradle.project.composite;

import org.gradle.api.initialization.Settings;
import org.gradle.initialization.IncludedBuildSpec;
import org.gradle.plugin.management.PluginManagementSpec;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class PluginManagementIncludeBuildSource implements IncludeBuildsSource {

    private Settings settings;

    public PluginManagementIncludeBuildSource(Settings settings) {
        this.settings = settings;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getIncludedBuilds() {
        try {
            PluginManagementSpec pluginManagement = settings.getPluginManagement();
            Method getIncludedBuildsMethod = pluginManagement.getClass().getDeclaredMethod("getIncludedBuilds");
            List<IncludedBuildSpec> includedBuildSpecs = (List<IncludedBuildSpec>) getIncludedBuildsMethod.invoke(pluginManagement);


            Path rootPath = settings.getRootDir().toPath();
            Field rootDirField = IncludedBuildSpec.class.getDeclaredField("rootDir");
            return includedBuildSpecs.stream().map(ibs -> {
                        try {
                            return rootDirField.get(ibs);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }).map(File.class::cast)
                    .map(File::toPath).map(path -> rootPath.relativize(path)).map(Object::toString).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
