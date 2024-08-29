package com.link_intersystems.gradle.publication.plugins;

import org.gradle.api.Project;

/**
 * A service provider interface for publication related utils used to extend the {@link PublicationUtilsExtension}.
 */
public interface PublicationUtil {
    void apply(Project project, PublicationServices publicationServices);
}
