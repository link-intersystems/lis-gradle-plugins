package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.Project;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class IncludePath {

    private final String value;

    public IncludePath(Path rootProjectRelativePath) {
        FileSystem fileSystem = rootProjectRelativePath.getFileSystem();
        String separator = fileSystem.getSeparator();
        String includePath = rootProjectRelativePath.toString().replaceAll(Pattern.quote(separator), Project.PATH_SEPARATOR);

        if (!includePath.startsWith(Project.PATH_SEPARATOR)) {
            includePath = Project.PATH_SEPARATOR + includePath;
        }

        this.value = includePath;
    }

    public String getValue() {
        return value;
    }
}
