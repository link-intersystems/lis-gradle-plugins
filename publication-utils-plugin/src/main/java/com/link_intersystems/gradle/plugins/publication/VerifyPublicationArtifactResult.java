package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.ArtifactCoordinates;

public class VerifyPublicationArtifactResult {

    private ArtifactCoordinates artifactCoordinates;
    private boolean verifyResult;

    public VerifyPublicationArtifactResult(ArtifactCoordinates artifactCoordinates, boolean verifyResult) {
        this.artifactCoordinates = artifactCoordinates;
        this.verifyResult = verifyResult;
    }

    public ArtifactCoordinates getArtifact() {
        return artifactCoordinates;
    }

    public boolean isSuccess() {
        return verifyResult;
    }
}

