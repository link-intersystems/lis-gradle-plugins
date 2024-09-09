package com.link_intersystems.gradle.project.sub;

import org.gradle.api.Project;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class IncludePath {

    private final String value;
    private final Path rootProjectRelativePath;

    public IncludePath(Path rootProjectRelativePath) {
        this.rootProjectRelativePath = rootProjectRelativePath;
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

    @Override
    public String toString() {
        return getValue();
    }

    public File getPath() {
        return rootProjectRelativePath.toFile();
    }
}
