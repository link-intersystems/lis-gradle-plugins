package com.link_intersystems.gradle.publication;

public interface ArtifactPublication {

    public Artifact getArtifact();

    public ArtifactRepository getArtifactRepository();

    public abstract String getArtifactName();

    String getArtifactRepositoryName();
}
