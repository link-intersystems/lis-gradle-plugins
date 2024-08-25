package com.link_intersystems.gradle.plugins.publication;

import org.gradle.api.publish.Publication;

public class AllPublicationFilter implements PublicationFilter {
    @Override
    public boolean accept(Publication publication) {
        return true;
    }
}
