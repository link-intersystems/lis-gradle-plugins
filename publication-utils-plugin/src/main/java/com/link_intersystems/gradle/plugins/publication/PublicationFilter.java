package com.link_intersystems.gradle.plugins.publication;

import org.gradle.api.publish.Publication;

public interface PublicationFilter {
    boolean accept(Publication publication);
}
