package com.link_intersystems.gradle.plugins.multimodule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradlePropertyConfigValuesTest {

    private SettingMocking settingMocking;
    private GradlePropertyConfigValues pluginProperties;
    private ProviderFactoryMocking providersMocking;

    @BeforeEach
    void setUp() {
        settingMocking = new SettingMocking();
        providersMocking = settingMocking.getProvidersMocking();
        pluginProperties = new GradlePropertyConfigValues(settingMocking.getSettings());
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