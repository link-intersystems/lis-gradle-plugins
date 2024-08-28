package com.link_intersystems.gradle.publication;

import com.link_intersystems.gradle.publication.plugins.ArtifactRepositoryDesc;

import java.util.List;

public interface ArtifactPublication {

    public List<? extends ArtifactCoordinates> getArtifactCoordinates();

    public ArtifactRepository getArtifactRepository();

    public abstract String getArtifactName();

    String getArtifactRepositoryName();

    ArtifactRepositoryDesc getArtifactRepositoryDesc();
}
