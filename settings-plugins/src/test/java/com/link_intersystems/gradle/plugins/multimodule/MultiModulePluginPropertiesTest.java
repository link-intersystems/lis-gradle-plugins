package com.link_intersystems.gradle.plugins.multimodule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiModulePluginPropertiesTest {

    private SettingMocking settingMocking;
    private MultiModulePluginProperties pluginProperties;
    private ProviderFactoryMocking providersMocking;

    @BeforeEach
    void setUp() {
        settingMocking = new SettingMocking();
        providersMocking = settingMocking.getProvidersMocking();
        pluginProperties = new MultiModulePluginProperties(settingMocking.getSettings());
    }

    @Test
    void excludePathsProperty() {
        providersMocking.setProperty("com.link-intersystems.gradle.multi-module.exclude-paths", "glob:**/mod*B:buildSrc:regex:mod???");

        List<String> excludePaths = pluginProperties.getExcludePaths();

        assertEquals(Arrays.asList("glob:**/mod*B", "buildSrc", "regex:mod???"),excludePaths);
    }

    @Test
    void excludePathsPropertyNotExists() {
        List<String> excludePaths = pluginProperties.getExcludePaths();

        assertEquals(Collections.emptyList(),excludePaths);
    }

    @Test
    void omitDefaultExcludesNotSet() {
        assertFalse(pluginProperties.isOmitDefaultExcludes());
    }

    @Test
    void omitDefaultExcludesSet() {
        providersMocking.setProperty("com.link-intersystems.gradle.multi-module.omit-default-excludes", "true");

        assertTrue(pluginProperties.isOmitDefaultExcludes());
    }

}