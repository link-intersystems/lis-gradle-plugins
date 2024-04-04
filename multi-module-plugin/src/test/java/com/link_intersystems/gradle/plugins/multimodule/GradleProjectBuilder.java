package com.link_intersystems.gradle.plugins.multimodule;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GradleProjectBuilder extends GradleModuleBuilder {

    public static GradleProjectBuilder rootProject(File projectRoot) throws IOException {
        assertTrue(new File(projectRoot, "settings.gradle.kts").createNewFile());
        assertTrue(new File(projectRoot, "build.gradle.kts").createNewFile());
        return new GradleProjectBuilder(projectRoot);
    }

    private final File projectRoot;

    public GradleProjectBuilder(File projectRoot) {
        super(projectRoot);
        this.projectRoot = projectRoot;
    }

    public GradleProjectBuilder withCompositeBuild(String path) throws IOException {
        File compositeBuildRoot = new File(projectRoot, path);
        assertTrue(compositeBuildRoot.mkdirs());
        return rootProject(compositeBuildRoot);
    }

}
