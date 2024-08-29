package com.link_intersystems.gradle.publication;

import com.link_intersystems.gradle.publication.plugins.ArtifactRepositoryDesc;

import java.util.List;

public interface ArtifactPublication {

    public abstract String getPublicationName();

    public List<? extends ArtifactCoordinates> getArtifactCoordinates();

    public ArtifactRepository getArtifactRepository();

    ArtifactRepositoryDesc getArtifactRepositoryDesc();
}
