package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProviderFactoryMocking {

    private ProviderFactory providerFactory;

    public ProviderFactoryMocking() {
        this.providerFactory = Mockito.mock(ProviderFactory.class);
        Property<String> absentProperty = mock(Property.class);
        when(absentProperty.isPresent()).thenReturn(false);
        when(providerFactory.gradleProperty(anyString())).thenReturn(absentProperty);
        when(providerFactory.environmentVariable(anyString())).thenReturn(absentProperty);
        when(providerFactory.systemProperty(anyString())).thenReturn(absentProperty);
    }

    public ProviderFactory getProviderFactory() {
        return providerFactory;
    }

    @SuppressWarnings("unchecked")
    public void setGradleProperty(String name, String value) {
        Property<String> property = mock(Property.class);
        when(property.isPresent()).thenReturn(true);
        when(property.get()).thenReturn(value);
        when(providerFactory.gradleProperty(name)).thenReturn(property);
    }

    @SuppressWarnings("unchecked")
    public void setSystemProperty(String name, String value) {
        Property<String> property = mock(Property.class);
        when(property.isPresent()).thenReturn(true);
        when(property.get()).thenReturn(value);
        when(providerFactory.systemProperty(name)).thenReturn(property);
    }

    @SuppressWarnings("unchecked")
    public void setEnvironmentVariable(String name, String value) {
        Property<String> property = mock(Property.class);
        when(property.isPresent()).thenReturn(true);
        when(property.get()).thenReturn(value);
        when(providerFactory.environmentVariable(name)).thenReturn(property);
    }
}
