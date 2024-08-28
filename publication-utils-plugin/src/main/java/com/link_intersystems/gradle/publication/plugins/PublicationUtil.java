package com.link_intersystems.gradle.publication.plugins;

import org.gradle.api.Project;

public interface PublicationUtil {
    void apply(Project project, PublicationServices publicationServices);
}
