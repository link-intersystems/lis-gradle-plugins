package com.link_intersystems.gradle.project.plugin;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectNamingStrategiesTest {

    private Path projectRoot;
    private Path subProjectPath;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        projectRoot = tempDir.resolve("projectRoot");
        subProjectPath = projectRoot.relativize(projectRoot.resolve("moduleA/api"));

    }

    @Test
    void hierarchical() {
        String projectName = ProjectNamingStrategies.HIERARCHICAL.getProjectName(projectRoot, subProjectPath);

        assertThat(projectName).isEqualTo("projectRoot:moduleA:api");
    }

    @Test
    void hierarchicalRootless() {
        String projectName = ProjectNamingStrategies.HIERARCHICAL_ROOTLESS.getProjectName(projectRoot, subProjectPath);

        assertThat(projectName).isEqualTo("moduleA:api");
    }

    @Test
    void flat() {
        String projectName = ProjectNamingStrategies.FLAT.getProjectName(projectRoot, subProjectPath);

        assertThat(projectName).isEqualTo("projectRoot-moduleA-api");
    }

    @Test
    void flatRootless() {
        String projectName = ProjectNamingStrategies.FLAT_ROOTLESS.getProjectName(projectRoot, subProjectPath);

        assertThat(projectName).isEqualTo("moduleA-api");
    }

    @Test
    void simple() {
        String projectName = ProjectNamingStrategies.SIMPLE.getProjectName(projectRoot, subProjectPath);

        assertThat(projectName).isEqualTo("api");
    }

}