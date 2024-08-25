package com.link_intersystems.gradle.publication.maven;

import org.gradle.api.publish.maven.MavenPublication;

public class MavenArtifactCoordinatesFactory {


    public MavenArtifactCoordinates create(MavenPublication publication, org.gradle.api.publish.maven.MavenArtifact mavenArtifact) {
        String groupId = publication.getGroupId();
        String artifactId = publication.getArtifactId();
        String version = publication.getVersion();
        String classifier = mavenArtifact.getClassifier();
        String extension = mavenArtifact.getExtension();

        return new MavenArtifactCoordinates(groupId, artifactId, version, extension, classifier);
    }
}
