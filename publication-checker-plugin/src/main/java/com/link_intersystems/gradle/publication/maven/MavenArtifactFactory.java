package com.link_intersystems.gradle.publication.maven;

import org.gradle.api.publish.maven.MavenPublication;

public class MavenArtifactFactory {


    public MavenArtifact create(MavenPublication publication, org.gradle.api.publish.maven.MavenArtifact mavenArtifact) {
        String groupId = publication.getGroupId();
        String artifactId = publication.getArtifactId();
        String version = publication.getVersion();
        String classifier = mavenArtifact.getClassifier();
        String extension = mavenArtifact.getExtension();

        return new MavenArtifact(groupId, artifactId, version, classifier, extension);
    }
}
