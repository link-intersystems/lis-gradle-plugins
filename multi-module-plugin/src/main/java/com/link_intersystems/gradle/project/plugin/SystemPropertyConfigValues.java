package com.link_intersystems.gradle.project.plugin;

import org.gradle.api.initialization.Settings;

class SystemPropertyConfigValues extends AbstractPropertiesConfigValues {

    public SystemPropertyConfigValues(Settings settings) {
        super(settings.getProviders()::systemProperty);
    }
}
