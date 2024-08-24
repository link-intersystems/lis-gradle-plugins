package com.link_intersystems.gradle.publication;

import com.link_intersystems.gradle.plugins.publication.ArtifactRepositoryDesc;

import java.util.List;

public interface ArtifactPublication {

    public List<? extends Artifact> getArtifacts();

    public ArtifactRepository getArtifactRepository();

    public abstract String getArtifactName();

    String getArtifactRepositoryName();

    ArtifactRepositoryDesc getArtifactRepositoryDesc();
}
