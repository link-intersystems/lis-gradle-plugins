package com.link_intersystems.gradle.publication.plugins.verify;

import com.link_intersystems.gradle.publication.plugins.ArtifactFilter;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import com.link_intersystems.gradle.publication.VersionProvider;
import org.gradle.api.publish.Publication;

public abstract class AbstractVerifyPublication<P extends Publication, T extends ArtifactCoordinates> implements VerifyPublication {

    private final String name;
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