package com.link_intersystems.gradle.plugins.publication.verify.maven;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MavenScopedVerifyRepositoryHandler implements MavenVerifyRepositoryHandler {

    public MavenArtifactRepository mavenCentral(Map<String, ?> args) {
        return repositoryHandler.mavenCentral(args);
    }

    @Override
    public MavenArtifactRepository mavenCentral() {
        return repositoryHandler.mavenCentral();
    }

    @Override
    public MavenArtifactRepository mavenCentral(Action<? super MavenArtifactRepository> action) {
        return repositoryHandler.mavenCentral(action);
    }

    @Override
    public MavenArtifactRepository mavenLocal() {
        return repositoryHandler.mavenLocal();
    }

    @Override
    public MavenArtifactRepository mavenLocal(Action<? super MavenArtifactRepository> action) {
        return repositoryHandler.mavenLocal(action);
    }

    @Override
    public MavenArtifactRepository maven(Closure closure) {
        return repositoryHandler.maven(closure);
    }

    @Override
    public MavenArtifactRepository maven(Action<? super MavenArtifactRepository> action) {
        return repositoryHandler.maven(action);
    }

    private RepositoryHandler repositoryHandler;

    public MavenScopedVerifyRepositoryHandler(RepositoryHandler repositoryHandler) {
        this.repositoryHandler = repositoryHandler;
    }

    @Override
    public List<ArtifactRepository> getArtifactRepositories() {
        return new ArrayList<>(repositoryHandler);
    }
}
