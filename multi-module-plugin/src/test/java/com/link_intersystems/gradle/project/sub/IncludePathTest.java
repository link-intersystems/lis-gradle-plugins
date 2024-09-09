package com.link_intersystems.gradle.project.sub;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IncludePathTest {

    @Test
    void getValue() {
        Path rootProjectPath = Paths.get("C:/projects/test-project");
        Path modulePath = Paths.get(rootProjectPath.toString(), "modules/moduleA");

        IncludePath includePath = new IncludePath(rootProjectPath.relativize(modulePath));

        assertEquals(":modules:moduleA", includePath.getValue());
    }
}