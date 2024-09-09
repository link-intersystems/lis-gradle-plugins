package com.link_intersystems.gradle.project.plugin;

import org.gradle.api.provider.Provider;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class AbstractPropertiesConfigValues implements ConfigValues {
    private final Function<String, Provider<String>> propertyProvider;

    public AbstractPropertiesConfigValues(Function<String, Provider<String>> propertyProvider) {
        this.propertyProvider = propertyProvider;
    }

    @Override
    public Boolean getDryRun() {
        String debug = getProperty("com.link-intersystems.gradle.multi-module.dryRun");
        return Boolean.parseBoolean(debug);
    }

    @Override
    public List<String> getExcludedPaths() {
        String excludePathsProperty = getProperty("com.link-intersystems.gradle.multi-module.exclude-paths");
        if (excludePathsProperty == null) {
            return null;
        }

        return Arrays.stream(excludePathsProperty.split("(?<!glob|regex):")).map(String::trim).collect(Collectors.toList());
    }

    public Boolean getOmitDefaultExcludes() {
        String omitDefaultExcludes = getProperty("com.link-intersystems.gradle.multi-module.omit-default-excludes");
        if (omitDefaultExcludes == null) {
            return null;
        }
        return Boolean.valueOf(omitDefaultExcludes);
    }

    private String getProperty(String name) {


        Provider<String> provider = propertyProvider.apply(name);
        if (provider.isPresent()) {
            return provider.get();
        }

        return null;
    }
}
