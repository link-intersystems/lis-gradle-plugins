package com.link_intersystems.gradle.publication;

import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.publish.Publication;

import java.util.*;

public abstract class ArtifactPublicationProvider {

    public static ArtifactPublicationProviders getProviders() {
        ServiceLoader<ArtifactPublicationProvider> serviceLoader = ServiceLoader.load(ArtifactPublicationProvider.class, ArtifactPublicationProvider.class.getClassLoader());
        Iterator<ArtifactPublicationProvider> artifactPublicationProviderIterator = serviceLoader.iterator();
        List<ArtifactPublicationProvider> artifactPublicationProviders = new ArrayList<>();
        artifactPublicationProviderIterator.forEachRemaining(artifactPublicationProviders::add);
        return new ArtifactPublicationProviders(artifactPublicationProviders);
    }

    public abstract ArtifactPublication tryCreateArtifactPublication(Publication publication, ArtifactRepository repository);
}
