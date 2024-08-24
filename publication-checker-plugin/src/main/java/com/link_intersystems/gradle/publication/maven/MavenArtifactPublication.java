package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactRepository;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.maven.internal.publisher.MavenNormalizedPublication;

public class MavenArtifactPublication implements ArtifactPublication {

    private final MavenPublicationInternal mavenPublication;
    private final org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository;

    public MavenArtifactPublication(MavenPublicationInternal mavenPublication, org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository) {
        this.mavenPublication = mavenPublication;
        this.mavenArtifactRepository = mavenArtifactRepository;
    }

    @Override
    public MavenArtifact getArtifact() {
        MavenNormalizedPublication normalisedPublication = mavenPublication.asNormalisedPublication();
        MavenArtifactFactory mavenArtifactFactory = new MavenArtifactFactory();
        return mavenArtifactFactory.create(mavenPublication, normalisedPublication.getMainArtifact());
    }

    @Override
    public ArtifactRepository getArtifactRepository() {
        return new MavenArtifactRepository(mavenArtifactRepository);
    }

    @Override
    public String getArtifactName() {
        return mavenPublication.getName();
    }

    @Override
    public String getArtifactRepositoryName() {
        return mavenArtifactRepository.getName();
    }
}
