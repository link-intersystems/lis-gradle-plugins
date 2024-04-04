package com.link_intersystems.gradle.plugins.multimodule;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.regex.Pattern;

class IncludeBuildPath {

    private final String value;

    public IncludeBuildPath(Path rootProjectRelativePath) {
        FileSystem fileSystem = rootProjectRelativePath.getFileSystem();
        String separator = fileSystem.getSeparator();
        this.value = rootProjectRelativePath.toString().replaceAll(Pattern.quote(separator), "/");
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
