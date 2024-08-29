package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.plugins.ArtifactRepositoryDesc;

import java.net.URI;

public class MavenArtifactRepositoryDesc implements ArtifactRepositoryDesc {

    public static MavenArtifactRepositoryDesc of(org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository) {
        return mavenArtifactRepository == null ? null : new MavenArtifactRepositoryDesc(mavenArtifactRepository.getName(), mavenArtifactRepository.getUrl());
    }

    private String name;
    private URI url;

    public MavenArtifactRepositoryDesc(String name, URI url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public URI getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return getName() + " (" + getUrl() + ")";
    }
}
