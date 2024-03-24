package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;
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
    private ProviderFactoryMocking providersMocking;
    private SettingMocking settingMocking;

    @BeforeEach
    void setUp(@TempDir File projectRoot) {
        this.projectRoot = projectRoot;

        settingMocking = new SettingMocking();
        settings = settingMocking.getSettings();
        ExtensionContainer extensions = mock(ExtensionContainer.class);
        when(settings.getExtensions()).thenReturn(extensions);
        when(settings.getRootDir()).thenReturn(projectRoot);

        providersMocking = settingMocking.getProvidersMocking();

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

        File moduleSubA = new File(moduleA, "subA");
        assertTrue(moduleSubA.mkdirs());
        assertTrue(new File(moduleSubA, "build.gradle.kts").createNewFile());

        File moduleB = new File(projectRoot, "modules/moduleB");
        assertTrue(moduleB.mkdirs());
        assertTrue(new File(moduleB, "build.gradle.kts").createNewFile());

        File moduleBC = new File(moduleB, "moduleC");
        assertTrue(moduleBC.mkdirs());
        assertTrue(new File(moduleBC, "build.gradle.kts").createNewFile());

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
        verify(settings, never()).includeBuild("modules/moduleA/subA");
        verify(settings).includeBuild("modules/moduleA");

        verify(settings, never()).include(":modules:moduleA:subA");
        verify(settings, never()).include(":moduleA:subA");
        verify(settings).include(":modules:moduleB");
        verify(settings).include(":modules:moduleB:moduleC");
    }

    @Test
    void excludePathsViaProperties() throws IOException {
        assertTrue(new File(projectRoot, "settings.gradle.kts").createNewFile());
        assertTrue(new File(projectRoot, "build.gradle.kts").createNewFile());

        File moduleA = new File(projectRoot, "modules/moduleA");
        assertTrue(moduleA.mkdirs());
        assertTrue(new File(moduleA, "build.gradle.kts").createNewFile());

        File moduleB = new File(projectRoot, "modules/moduleB");
        assertTrue(moduleB.mkdirs());
        assertTrue(new File(moduleB, "build.gradle.kts").createNewFile());

        File buildSrc = new File(projectRoot, "buildSrc");
        assertTrue(buildSrc.mkdirs());
        assertTrue(new File(buildSrc, "settings.gradle.kts").createNewFile());

        providersMocking.setProperty("com.link-intersystems.gradle.multi-module.exclude-paths", "glob:**/mod*B:buildSrc");

        multiModulePlugin.apply(settings);

        verify(settings).include(":modules:moduleA");
        verify(settings, never()).include(":modules:moduleB");
    }

}