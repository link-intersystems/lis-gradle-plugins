package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.ArtifactCoordinates;

public class VerifyPublicationArtifactResult {

    private ArtifactCoordinates artifactCoordinates;
    private boolean exists;

    public VerifyPublicationArtifactResult(ArtifactCoordinates artifactCoordinates, boolean exists) {
        this.artifactCoordinates = artifactCoordinates;
        this.exists = exists;
    }

    public ArtifactCoordinates getArtifact() {
        return artifactCoordinates;
    }

    public boolean isSuccess() {
        return exists;
    }

    @Override
    public String toString() {
        return artifactCoordinates + " " + (exists ? "exists" : "not exists");
    }
}

