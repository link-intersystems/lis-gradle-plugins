package com.link_intersystems.gradle.project.plugin;

import org.gradle.api.provider.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    @Override
    public ProjectNamingStrategy getProjectNamingStrategy() {
        String projectNamingStrategy = getProperty("com.link-intersystems.gradle.multi-module.project-naming-strategy");

        if (projectNamingStrategy == null) {
            return null;
        }

        try {
            Class<?> projectNamingStrategyClass = Class.forName(projectNamingStrategy);
            Constructor<?> defaultConstructor = projectNamingStrategyClass.getDeclaredConstructor();
            return (ProjectNamingStrategy) defaultConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Misconfigured com.link-intersystems.gradle.multi-module.project-naming-strategy", e);
        }
    }

    private String getProperty(String name) {


        Provider<String> provider = propertyProvider.apply(name);
        if (provider.isPresent()) {
            return provider.get();
        }

        return null;
    }
}
