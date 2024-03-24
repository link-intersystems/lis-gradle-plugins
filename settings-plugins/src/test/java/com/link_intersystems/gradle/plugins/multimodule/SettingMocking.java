package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SettingMocking {

    private Settings settings;
    private ProviderFactoryMocking providerFactoryMocking;

    public SettingMocking() {
        this.settings = mock(Settings.class);
    }

    public Settings getSettings() {
        return settings;
    }

    ProviderFactoryMocking getProvidersMocking() {
        if (providerFactoryMocking == null) {
            providerFactoryMocking = new ProviderFactoryMocking();
            ProviderFactory providerFactory = providerFactoryMocking.getProviderFactory();
            when(settings.getProviders()).thenReturn(providerFactory);
        }
        return providerFactoryMocking;
    }
}
