package com.link_intersystems.gradle.publication.plugins.verify;

import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import com.link_intersystems.gradle.publication.VersionProvider;
import com.link_intersystems.gradle.publication.plugins.ArtifactFilter;
import org.gradle.api.publish.Publication;

import java.util.List;

public abstract class AbstractVerifyPublication<P extends Publication, T extends ArtifactCoordinates> implements VerifyPublication {

    private final String name;
    private List<String> artifacts;
    private VerifyRepositoryHandler repositories;
    private VerifyPublicationResultHandler mode;
    private ArtifactFilter<T> artifactFilter;
    private P publication;
    private VersionProvider versionProvider;

    public AbstractVerifyPublication(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<String> getArtifacts() {
        return artifacts;
    }

    @Override
    public void setArtifacts(List<String> artifacts) {
        this.artifacts = artifacts;
    }

    @Override
    public void setResultHandler(VerifyPublicationResultHandler mode) {
        this.mode = mode;
    }

    @Override
    public VerifyPublicationResultHandler getResultHandler() {
        return mode;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(P publication) {
        this.publication = publication;
    }

    public void setArtifactFilter(ArtifactFilter<T> artifactFilter) {
        this.artifactFilter = artifactFilter;
    }

    @Override
    public ArtifactFilter<T> getArtifactFilter() {
        return artifactFilter;
    }

    @Override
    public VersionProvider getVersionProvider() {
        return versionProvider;
    }

    @Override
    public void setVersionProvider(VersionProvider versionProvider) {
        this.versionProvider = versionProvider;
    }
}
