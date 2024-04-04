package com.link_intersystems.gradle.plugins.multimodule;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GradleModuleBuilder {
    private final File moduleRoot;

    public GradleModuleBuilder(File moduleRoot) {
        this.moduleRoot = moduleRoot;
    }

    public GradleModuleBuilder withModule(String path) throws IOException {
        File moduleRoot = new File(this.moduleRoot, path);
        assertTrue(moduleRoot.mkdirs());
        assertTrue(new File(moduleRoot, "build.gradle.kts").createNewFile());
        return new GradleModuleBuilder(moduleRoot);
    }
}
