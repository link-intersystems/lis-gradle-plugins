package com.link_intersystems.gradle.plugins.multimodule;

import java.util.List;

interface ConfigValues {
    List<String> getExcludedPaths();

    Boolean getOmitDefaultExcludes();

    Boolean getDryRun();
}
