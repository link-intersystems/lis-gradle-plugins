package com.link_intersystems.gradle.project.plugin;

import java.util.List;

public interface ConfigValues {
    List<String> getExcludedPaths();

    Boolean getOmitDefaultExcludes();

    Boolean getDryRun();

    ProjectNamingStrategy getProjectNamingStrategy();
}
