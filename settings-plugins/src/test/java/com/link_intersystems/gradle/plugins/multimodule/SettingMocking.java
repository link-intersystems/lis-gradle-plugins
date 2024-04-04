package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.ProviderFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SettingMocking {

    private final GradleMocking gradleMocking;
    private Settings settings;
    private ProviderFactoryMocking providerFactoryMocking;
    private ExtensionContainerMocking extensionContainerMocking;

    public SettingMocking() {
        this(new GradleMocking());
    }

    public SettingMocking(GradleMocking gradleMocking) {
        this.settings = mock(Settings.class);
        this.gradleMocking = gradleMocking;
        extensionContainerMocking = new ExtensionContainerMocking();
        when(settings.getExtensions()).thenReturn(extensionContainerMocking.getExtensionContainer());
        when(settings.getGradle()).thenReturn(gradleMocking.getGradle());
    }

    public ExtensionContainerMocking getExtensionContainerMocking() {
        return extensionContainerMocking;
    }

    public GradleMocking getGradleMocking() {
        return gradleMocking;
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
