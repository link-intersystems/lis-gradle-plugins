package com.link_intersystems.gradle.project.plugin;

import java.util.List;

interface ConfigValues {
    List<String> getExcludedPaths();

    Boolean getOmitDefaultExcludes();

    Boolean getDryRun();
}
