package com.link_intersystems.gradle.publication;

import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.publish.Publication;

import java.util.*;

public class ArtifactPublicationProviders extends AbstractList<ArtifactPublicationProvider> {

    public static ArtifactPublicationProviders get() {
        ServiceLoader<ArtifactPublicationProvider> serviceLoader = ServiceLoader.load(ArtifactPublicationProvider.class, ArtifactPublicationProvider.class.getClassLoader());
        Iterator<ArtifactPublicationProvider> artifactPublicationProviderIterator = serviceLoader.iterator();
        List<ArtifactPublicationProvider> artifactPublicationProviders = new ArrayList<>();
        artifactPublicationProviderIterator.forEachRemaining(artifactPublicationProviders::add);
        return new ArtifactPublicationProviders(artifactPublicationProviders);
    }

    private List<ArtifactPublicationProvider> providers;

    ArtifactPublicationProviders(List<ArtifactPublicationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public ArtifactPublicationProvider get(int index) {
        return providers.get(index);
    }

    @Override
    public int size() {
        return providers.size();
    }

    public Optional<ArtifactPublication> createArtifactPublication(Publication publication, ArtifactRepository repository, VersionProvider versionProvider) {
        return providers.stream().map(p -> p.tryCreateArtifactPublication(publication, repository, versionProvider)).findFirst();
    }
}
