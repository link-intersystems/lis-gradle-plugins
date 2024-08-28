package com.link_intersystems.gradle.plugins.publication.utils;

import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.ExtensionContainer;

public interface PublicationServices {
    ExtensionContainer getUtilsExtensionContainer();

    RepositoryHandler createRepositoryHandler();
}
