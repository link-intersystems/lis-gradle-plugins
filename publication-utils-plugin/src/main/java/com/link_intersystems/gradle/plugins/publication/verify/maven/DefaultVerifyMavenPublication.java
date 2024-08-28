package com.link_intersystems.gradle.plugins.publication.verify.maven;

import com.link_intersystems.gradle.plugins.publication.verify.AbstractVerifyPublication;
import com.link_intersystems.gradle.publication.maven.MavenArtifactCoordinates;
import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.maven.MavenPublication;

public class DefaultVerifyMavenPublication extends AbstractVerifyPublication<MavenPublication, MavenArtifactCoordinates> implements VerifyMavenPublication {

    private MavenVerifyRepositoryHandler verifyRepositoriesHandler;

    public DefaultVerifyMavenPublication(String name, RepositoryHandler repositoryHandler) {
        super(name);
        verifyRepositoriesHandler = new MavenScopedVerifyRepositoryHandler(repositoryHandler);
    }

    public MavenVerifyRepositoryHandler getVerifyRepositories() {
        return verifyRepositoriesHandler;
    }

    public void verifyRepositories(Action<? super MavenVerifyRepositoryHandler> configure) {
        configure.execute(getVerifyRepositories());
    }
}
