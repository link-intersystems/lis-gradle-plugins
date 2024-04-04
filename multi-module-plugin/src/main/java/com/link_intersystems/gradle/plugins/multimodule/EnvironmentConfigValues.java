package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;

class EnvironmentConfigValues extends AbstractPropertiesConfigValues {

    public EnvironmentConfigValues(Settings settings) {
        super(settings.getProviders()::environmentVariable);
    }
}
