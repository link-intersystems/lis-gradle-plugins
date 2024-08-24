package com.link_intersystems.gradle.publication;

import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.publish.Publication;

import java.util.AbstractList;
import java.util.List;
import java.util.Optional;

public class ArtifactPublicationProviders extends AbstractList<ArtifactPublicationProvider> {

    private List<ArtifactPublicationProvider> providers;

    public ArtifactPublicationProviders(List<ArtifactPublicationProvider> providers) {
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


    public Optional<ArtifactPublication> createArtifactPublication(Publication publication, ArtifactRepository repository) {
        Optional<ArtifactPublication> artifactPublication = providers.stream().map(p -> p.tryCreateArtifactPublication(publication, repository)).findFirst();
        return artifactPublication;
    }
}
