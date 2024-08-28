package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactRepositoryDesc;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class VerifyPublicationResult {

    private List<VerifyPublicationArtifactResult> artifactResults;
    private ArtifactRepositoryDesc artifactRepositoryDesc;

    public VerifyPublicationResult(ArtifactRepositoryDesc repositoryDesc, List<VerifyPublicationArtifactResult> artifactResults) {
        this.artifactResults = requireNonNull(artifactResults);
        this.artifactRepositoryDesc = requireNonNull(repositoryDesc);
    }

    public ArtifactRepositoryDesc getArtifactRepositoryDesc() {
        return artifactRepositoryDesc;
    }

    public List<VerifyPublicationArtifactResult> getArtifactResults() {
        return artifactResults;
    }
}
