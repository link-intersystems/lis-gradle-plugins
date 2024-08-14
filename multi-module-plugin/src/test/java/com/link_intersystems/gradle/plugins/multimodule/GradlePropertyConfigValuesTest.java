package com.link_intersystems.gradle.plugins.multimodule;

import com.link_intersystems.gradle.api.initialization.SettingsMocking;
import com.link_intersystems.gradle.api.provider.ProviderFactoryMocking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradlePropertyConfigValuesTest {

    private SettingsMocking settingsMocking;
    private GradlePropertyConfigValues pluginProperties;
    private ProviderFactoryMocking providersMocking;

    @BeforeEach
    void setUp() {
        settingsMocking = new SettingsMocking();
        providersMocking = settingsMocking.getProvidersMocking();
        pluginProperties = new GradlePropertyConfigValues(settingsMocking.getSettings());
    }

    @Test
    void excludePathsProperty() {
        providersMocking.setGradleProperty("com.link-intersystems.gradle.multi-module.exclude-paths", "glob:**/mod*B:buildSrc:regex:mod???");

        List<String> excludePaths = pluginProperties.getExcludedPaths();

        assertEquals(Arrays.asList("glob:**/mod*B", "buildSrc", "regex:mod???"),excludePaths);
    }

    @Test
    void excludePathsPropertyNotExists() {
        List<String> excludePaths = pluginProperties.getExcludedPaths();

        assertNull(excludePaths);
    }

    @Test
    void omitDefaultExcludesNotSet() {
        assertNull(pluginProperties.getOmitDefaultExcludes());
    }

    @Test
    void omitDefaultExcludesSet() {
        providersMocking.setGradleProperty("com.link-intersystems.gradle.multi-module.omit-default-excludes", "true");

        assertTrue(pluginProperties.getOmitDefaultExcludes());
    }

}