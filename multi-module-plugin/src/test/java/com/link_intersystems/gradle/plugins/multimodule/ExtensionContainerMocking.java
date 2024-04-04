package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.api.plugins.ExtensionContainer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExtensionContainerMocking {

    public static interface OnCreate<T> {

        void returnValue(T value);
    }

    private ExtensionContainer extensionContainer;
    private ProviderFactoryMocking providerFactoryMocking;

    public ExtensionContainerMocking() {

        this.extensionContainer = mock(ExtensionContainer.class);
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public <T> OnCreate<T> onCreate(String extensionName, Class<T> type) {
        return value -> {
            when(extensionContainer.create(type, extensionName, type)).thenReturn(value);
        };
    }
}
