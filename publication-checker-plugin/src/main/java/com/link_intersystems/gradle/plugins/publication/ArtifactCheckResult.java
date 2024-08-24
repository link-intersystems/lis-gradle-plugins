package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.Artifact;

public class ArtifactCheckResult {

    private Artifact artifact;
    private boolean existent;

    public ArtifactCheckResult(Artifact artifact, boolean existent) {
        this.artifact = artifact;
        this.existent = existent;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public boolean isExistent() {
        return existent;
    }
}

