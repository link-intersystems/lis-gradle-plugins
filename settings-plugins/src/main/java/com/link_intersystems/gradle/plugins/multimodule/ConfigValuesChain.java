package com.link_intersystems.gradle.plugins.multimodule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ConfigValuesChain implements ConfigValues {

    private List<ConfigValues> configValues = new ArrayList<>();

    public ConfigValuesChain(ConfigValues... configValues) {
        this.configValues.addAll(Arrays.asList(configValues));
    }

    @Override
    public List<String> getExcludedPaths() {
        for (ConfigValues configValue : configValues) {
            List<String> excludedPaths = configValue.getExcludedPaths();
            if (excludedPaths != null) {
                return excludedPaths;
            }
        }

        return null;
    }

    @Override
    public Boolean getOmitDefaultExcludes() {
        for (ConfigValues configValue : configValues) {
            Boolean omitDefaultExcludes = configValue.getOmitDefaultExcludes();
            if (omitDefaultExcludes != null) {
                return omitDefaultExcludes;
            }
        }

        return null;
    }

    @Override
    public Boolean getDryRun() {
        for (ConfigValues configValue : configValues) {
            Boolean dryRun = configValue.getDryRun();
            if (dryRun != null) {
                return dryRun;
            }
        }

        return null;
    }
}
