package com.link_intersystems.gradle.plugins.publication.utils;

import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.internal.reflect.Instantiator;

public interface PublicationUtil {
    void apply(Project project, PublicationServices publicationServices);
}
