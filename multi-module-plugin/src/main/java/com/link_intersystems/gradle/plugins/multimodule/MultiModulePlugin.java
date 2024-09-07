package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.ExtensionContainer;

public class MultiModulePlugin implements Plugin<Settings> {

    @Override
    public void apply(Settings s) {

        ExtensionContainer extensions = s.getExtensions();
        ConfigValues settingsConfigValues = extensions.create(MultiModuleConfig.class, "multiModule", MultiModuleConfig.class);

        ConfigValuesChain multiModuleConfigValuesChain = new ConfigValuesChain(
                settingsConfigValues,
                new GradlePropertyConfigValues(s),
                new SystemPropertyConfigValues(s),
                new EnvironmentConfigValues(s),
                new DefaultConfigValues()
        );

        MultiModuleConfigurationAction configurationAction = new MultiModuleConfigurationAction(multiModuleConfigValuesChain);
        s.getGradle().settingsEvaluated(configurationAction);
    }
}
