package com.link_intersystems.gradle.project.plugin;

import java.nio.file.Path;

public interface ProjectNamingStrategy {

    public String getProjectName(Path rootDirectory, Path rootRelativeProjectPath);
}
