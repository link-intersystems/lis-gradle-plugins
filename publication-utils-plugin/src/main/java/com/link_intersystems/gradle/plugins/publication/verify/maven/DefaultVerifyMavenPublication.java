package com.link_intersystems.gradle.plugins.publication.verify.maven;

import com.link_intersystems.gradle.plugins.publication.verify.AbstractVerifyPublication;
import com.link_intersystems.gradle.publication.maven.MavenArtifactCoordinates;
import org.gradle.api.artifacts.dsl.RepositoryHandler;

public class DefaultVerifyMavenPublication extends AbstractVerifyPublication<MavenArtifactCoordinates> implements VerifyMavenPublication {

    public DefaultVerifyMavenPublication(String name, RepositoryHandler repositories) {
        super(name, repositories);
    }
}
