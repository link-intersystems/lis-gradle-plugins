package com.link_intersystems.gradle.project.plugin;

import com.link_intersystems.gradle.api.SettingsMocking;
import com.link_intersystems.gradle.api.GradleMocking;
import com.link_intersystems.gradle.api.ExtensionContainerMocking;
import com.link_intersystems.gradle.api.ProviderFactoryMocking;
import com.link_intersystems.gradle.project.builder.GradleProjectBuilder;
import org.gradle.api.initialization.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

class SettingsMultiModulePluginExtensionTest {

    private MultiModulePlugin multiModulePlugin;
    private Settings settings;
    private ProviderFactoryMocking providersMocking;
    private SettingsMocking settingsMocking;
    private GradleProjectBuilder projectBuilder;

    @BeforeEach
    void setUp(@TempDir Path projectRoot) throws IOException {

        GradleMocking gradleMocking = new GradleMocking();
        settingsMocking = gradleMocking.getSettingsMocking();
        settings = settingsMocking.getSettings();
        when(settings.getRootDir()).thenReturn(projectRoot.toFile());

        providersMocking = settingsMocking.getProvidersMocking();

        multiModulePlugin = new MultiModulePlugin();

        ExtensionContainerMocking extensionContainerMocking = settingsMocking.getExtensionContainerMocking();
        extensionContainerMocking.onCreate("multiModule", MultiModuleExtension.class).returnValue(new MultiModuleExtension());

        projectBuilder = GradleProjectBuilder.rootProject(projectRoot);
    }

    private void applyMultiModulePlugin() {
        multiModulePlugin.apply(settings);
        GradleMocking gradleMocking = settingsMocking.getGradleMocking();
        gradleMocking.execSettingsEvaluated();
    }

    @Test
    void apply() throws IOException {
        projectBuilder.createCompositeBuild("buildSrc");
        projectBuilder.createCompositeBuild("modules/moduleA").createSubproject("subA");
        projectBuilder.createSubproject("modules/moduleB");
        projectBuilder.createSubproject("modules/moduleB/moduleC");
        projectBuilder.createCompositeBuild("modules/.hiddenModuleA");

        applyMultiModulePlugin();

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
        projectBuilder.createCompositeBuild("buildSrc");
        projectBuilder.createSubproject("modules/moduleA");
        projectBuilder.createSubproject("modules/moduleB");

        providersMocking.setGradleProperty("com.link-intersystems.gradle.multi-module.exclude-paths", "glob:**/mod*B:buildSrc");

        applyMultiModulePlugin();

        verify(settings).include(":modules:moduleA");
        verify(settings, never()).include(":modules:moduleB");
    }

    @Test
    void dryRun() throws IOException {
        projectBuilder.createCompositeBuild("buildSrc");
        projectBuilder.createCompositeBuild("modules/moduleA").createSubproject("subA");
        projectBuilder.createSubproject("modules/moduleB");
        projectBuilder.createSubproject("modules/moduleB/moduleC");
        projectBuilder.createCompositeBuild("modules/.hiddenModuleA");

        providersMocking.setGradleProperty("com.link-intersystems.gradle.multi-module.dryRun", "true");

        applyMultiModulePlugin();

        verify(settings, never()).includeBuild("");
        verify(settings, never()).includeBuild("buildSrc");
        verify(settings, never()).includeBuild("modules/.hiddenModuleA");
        verify(settings, never()).includeBuild("modules/moduleB");
        verify(settings, never()).includeBuild("modules/moduleA/subA");
        verify(settings, never()).includeBuild("modules/moduleA");

        verify(settings, never()).include(":modules:moduleA:subA");
        verify(settings, never()).include(":moduleA:subA");
        verify(settings, never()).include(":modules:moduleB");
        verify(settings, never()).include(":modules:moduleB:moduleC");
    }
}