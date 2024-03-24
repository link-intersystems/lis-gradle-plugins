package com.link_intersystems.gradle.plugins.multimodule;

import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class IncludeBuildPathTest {

    @Test
    void getValue() {
        Path rootProjectPath = Paths.get("C:/projects/test-project");
        Path modulePath = Paths.get(rootProjectPath.toString(), "modules/moduleA");

        IncludeBuildPath includeBuildPath = new IncludeBuildPath(rootProjectPath.relativize(modulePath));

        assertEquals("modules/moduleA", includeBuildPath.getValue());
    }
}