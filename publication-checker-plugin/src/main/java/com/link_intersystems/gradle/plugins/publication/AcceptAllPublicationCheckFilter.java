package com.link_intersystems.gradle.plugins.publication;

import org.gradle.api.publish.Publication;

public class AcceptAllPublicationCheckFilter implements PublicationCheckFilter {
    @Override
    public boolean accept(Publication publication) {
        return true;
    }
}
