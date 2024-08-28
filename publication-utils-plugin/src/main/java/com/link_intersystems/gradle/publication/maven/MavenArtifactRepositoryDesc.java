package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.plugins.ArtifactRepositoryDesc;

import java.net.URI;

public class MavenArtifactRepositoryDesc implements ArtifactRepositoryDesc {

    private org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository;

    public MavenArtifactRepositoryDesc(org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository) {
        this.mavenArtifactRepository = mavenArtifactRepository;
    }

    @Override
    public String getName() {
        return mavenArtifactRepository.getName();
    }

    @Override
    public URI getUrl() {
        return mavenArtifactRepository.getUrl();
    }

    @Override
    public String toString() {
        return getName() + " (" + getUrl() + ")";
    }
}
