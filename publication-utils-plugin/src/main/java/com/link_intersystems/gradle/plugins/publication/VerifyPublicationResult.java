package com.link_intersystems.gradle.plugins.publication;

import java.util.List;

public class VerifyPublicationResult {

    private List<VerifyPublicationArtifactResult> verifyPublicationArtifactResults;
    private ArtifactRepositoryDesc artifactRepositoryDesc;

    public VerifyPublicationResult(ArtifactRepositoryDesc repositoryDesc, List<VerifyPublicationArtifactResult> checkResults) {
        this.verifyPublicationArtifactResults = checkResults;
        this.artifactRepositoryDesc = repositoryDesc;
    }

    public ArtifactRepositoryDesc getArtifactRepositoryDesc() {
        return artifactRepositoryDesc;
    }

    public List<VerifyPublicationArtifactResult> getArtifactCheckResults() {
        return verifyPublicationArtifactResults;
    }
}
