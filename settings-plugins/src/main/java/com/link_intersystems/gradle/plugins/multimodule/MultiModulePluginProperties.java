package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultiModulePluginProperties {


    private final ProviderFactory providers;

    public MultiModulePluginProperties(Settings settings) {
        providers = settings.getProviders();
    }

    public List<String> getExcludePaths() {
        String excludePathsProperty = getProperty("com.link-intersystems.gradle.multi-module.exclude-paths");
        if (excludePathsProperty == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(excludePathsProperty.split("(?<!glob|regex):")).map(String::trim).collect(Collectors.toList());
    }

    public boolean isOmitDefaultExcludes() {
        String omitDefaultExcludes = getProperty("com.link-intersystems.gradle.multi-module.omit-default-excludes", "false");
        return Boolean.parseBoolean(omitDefaultExcludes);
    }

    private String getProperty(String name) {
        return getProperty(name, null);
    }


    private String getProperty(String name, String defaultValue) {
        Provider<String> property = providers.gradleProperty(name);
        if (property.isPresent()) {
            return property.get();
        }
        return defaultValue;
    }
}
