package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.plugins.publication.VersionProvider;
import org.gradle.api.publish.maven.MavenPublication;

public class MavenArtifactCoordinatesFactory {

    private final VersionProvider versionProvider;

    public MavenArtifactCoordinatesFactory(VersionProvider versionProvider) {
        this.versionProvider = versionProvider;
    }

    public MavenArtifactCoordinates create(MavenPublication publication, org.gradle.api.publish.maven.MavenArtifact mavenArtifact) {
        String groupId = publication.getGroupId();
        String artifactId = publication.getArtifactId();
        String version = publication.getVersion();
        version = versionProvider.getVersion(groupId, artifactId, version);
        String classifier = mavenArtifact.getClassifier();
        String extension = mavenArtifact.getExtension();

        return new MavenArtifactCoordinates(groupId, artifactId, version, extension, classifier);
    }
}
