package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.ExtensionContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MultiModulePluginTest {

    private File projectRoot;
    private MultiModulePlugin multiModulePlugin;
    private Settings settings;

    @BeforeEach
    void setUp(@TempDir File projectRoot) {
        this.projectRoot = projectRoot;

        settings = mock(Settings.class);
        ExtensionContainer extensions = mock(ExtensionContainer.class);
        when(settings.getExtensions()).thenReturn(extensions);
        when(settings.getRootDir()).thenReturn(projectRoot);

        multiModulePlugin = new MultiModulePlugin();
    }

    @Test
    void apply() throws IOException {
        assertTrue(new File(projectRoot, "settings.gradle.kts").createNewFile());
        assertTrue(new File(projectRoot, "build.gradle.kts").createNewFile());

        File moduleA = new File(projectRoot, "modules/moduleA");
        assertTrue(moduleA.mkdirs());
        assertTrue(new File(moduleA, "settings.gradle.kts").createNewFile());
        assertTrue(new File(moduleA, "build.gradle.kts").createNewFile());

        File moduleB = new File(projectRoot, "modules/moduleB");
        assertTrue(moduleB.mkdirs());
        assertTrue(new File(moduleB, "build.gradle.kts").createNewFile());

        File hiddenModuleA = new File(projectRoot, "modules/.hiddenModuleA");
        assertTrue(hiddenModuleA.mkdirs());
        assertTrue(new File(hiddenModuleA, "settings.gradle.kts").createNewFile());

        File buildSrc = new File(projectRoot, "buildSrc");
        assertTrue(buildSrc.mkdirs());
        assertTrue(new File(buildSrc, "settings.gradle.kts").createNewFile());

        multiModulePlugin.apply(settings);

        verify(settings, never()).includeBuild("");
        verify(settings, never()).includeBuild("buildSrc");
        verify(settings, never()).includeBuild("modules/.hiddenModuleA");
        verify(settings, never()).includeBuild("modules/moduleB");
        verify(settings).includeBuild("modules/moduleA");
    }
}