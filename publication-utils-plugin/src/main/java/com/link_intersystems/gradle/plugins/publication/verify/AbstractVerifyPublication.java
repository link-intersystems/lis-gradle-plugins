package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.Publication;

public abstract class AbstractVerifyPublication<T extends ArtifactCoordinates> implements VerifyPublication {

    protected final String name;
    protected RepositoryHandler repositories;
    private Publication publication;
    private VerifyMode mode;
    private ArtifactFilter<T> artifactFilter;

    public AbstractVerifyPublication(String name, RepositoryHandler repositories) {
        this.name = name;
        this.repositories = repositories;
    }

    public String getName() {
        return name;
    }

    public void setMode(VerifyMode mode) {
        this.mode = mode;
    }

    public VerifyMode getMode() {
        return mode;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public RepositoryHandler getVerifyRepositories() {
        return repositories;
    }

    public void verifyRepositories(Action<? super RepositoryHandler> configure) {
        configure.execute(getVerifyRepositories());
    }

    public void setArtifactFilter(ArtifactFilter<T> artifactFilter) {
        this.artifactFilter = artifactFilter;
    }

    @Override
    public ArtifactFilter<T> getArtifactFilter() {
        return artifactFilter;
    }

}
