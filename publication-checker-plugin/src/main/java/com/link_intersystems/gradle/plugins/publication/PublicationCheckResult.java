package com.link_intersystems.gradle.plugins.publication;

import java.util.List;

public class PublicationCheckResult {

    private List<ArtifactCheckResult> artifactCheckResults;
    private ArtifactRepositoryDesc artifactRepositoryDesc;

    public PublicationCheckResult(List<ArtifactCheckResult> artifactCheckResults, ArtifactRepositoryDesc artifactRepositoryDesc) {
        this.artifactCheckResults = artifactCheckResults;
        this.artifactRepositoryDesc = artifactRepositoryDesc;
    }

    public ArtifactRepositoryDesc getArtifactRepositoryDesc() {
        return artifactRepositoryDesc;
    }

    public List<ArtifactCheckResult> getArtifactCheckResults() {
        return artifactCheckResults;
    }
}
