package com.link_intersystems.gradle.project.plugin;

import java.nio.file.Path;

public interface ProjectNamingStrategy {

    default public String getProjectName(Path rootDirectory, Path rootRelativeProjectPath) {
        return rootRelativeProjectPath.getFileName().toString();
    }
}
