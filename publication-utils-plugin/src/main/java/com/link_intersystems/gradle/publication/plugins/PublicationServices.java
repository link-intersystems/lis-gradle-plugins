package com.link_intersystems.gradle.publication.plugins;

import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.ExtensionContainer;

public interface PublicationServices {
    ExtensionContainer getUtilsExtensionContainer();

    RepositoryHandler createRepositoryHandler();
}
