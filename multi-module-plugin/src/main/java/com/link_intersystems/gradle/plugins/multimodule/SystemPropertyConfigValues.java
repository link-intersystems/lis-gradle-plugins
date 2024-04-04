package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;

class SystemPropertyConfigValues extends AbstractPropertiesConfigValues {

    public SystemPropertyConfigValues(Settings settings) {
        super(settings.getProviders()::systemProperty);
    }
}
