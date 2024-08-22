package com.link_intersystems.gradle.plugins.publishing.check;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishingCheckPlugin implements Plugin<Project> {

    private Logger logger = LoggerFactory.getLogger(PublishingCheckPlugin.class);

    @Override
    public void apply(Project p) {
        PublishingExtension publishingExtension = p.getExtensions().findByType(PublishingExtension.class);
        PublicationContainer publications = publishingExtension.getPublications();

        if(publications != null){

        }
    }

}
