package com.link_intersystems.gradle.project.plugin;

import org.gradle.api.initialization.Settings;

class GradlePropertyConfigValues extends AbstractPropertiesConfigValues {


    public GradlePropertyConfigValues(Settings settings) {
        super(settings.getProviders()::gradleProperty);
    }
}
