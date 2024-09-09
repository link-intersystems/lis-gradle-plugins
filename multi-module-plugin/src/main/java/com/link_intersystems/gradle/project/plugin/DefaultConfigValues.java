package com.link_intersystems.gradle.project.plugin;

import java.util.Collections;
import java.util.List;

public class DefaultConfigValues implements ConfigValues {
    @Override
    public List<String> getExcludedPaths() {
        return Collections.emptyList();
    }

    @Override
    public Boolean getOmitDefaultExcludes() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean getDryRun() {
        return Boolean.FALSE;
    }
}
