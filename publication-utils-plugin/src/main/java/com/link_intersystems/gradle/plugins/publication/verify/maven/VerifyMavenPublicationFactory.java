package com.link_intersystems.gradle.plugins.publication.verify.maven;

import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.artifacts.dsl.RepositoryHandler;

public class VerifyMavenPublicationFactory implements NamedDomainObjectFactory<VerifyMavenPublication> {

    private RepositoryHandler repositories;

    public VerifyMavenPublicationFactory(RepositoryHandler repositories) {
        this.repositories = repositories;
    }

    @Override
    public VerifyMavenPublication create(String name) {
        return new DefaultVerifyMavenPublication(name, repositories);
    }
}
