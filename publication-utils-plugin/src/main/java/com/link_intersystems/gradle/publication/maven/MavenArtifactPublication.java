package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.plugins.ArtifactRepositoryDesc;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactRepository;
import com.link_intersystems.gradle.publication.VersionProvider;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.maven.internal.publisher.MavenNormalizedPublication;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MavenArtifactPublication implements ArtifactPublication {

    private final MavenPublicationInternal mavenPublication;
    private final org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository;
    private final VersionProvider versionProvider;

    public MavenArtifactPublication(MavenPublicationInternal mavenPublication, org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository, VersionProvider versionProvider) {
        this.mavenPublication = mavenPublication;
        this.mavenArtifactRepository = mavenArtifactRepository;
        this.versionProvider = versionProvider;
    }

    @Override
    public List<? extends ArtifactCoordinates> getArtifactCoordinates() {
        MavenNormalizedPublication normalisedPublication = mavenPublication.asNormalisedPublication();
        MavenArtifactCoordinatesFactory mavenArtifactCoordinatesFactory = new MavenArtifactCoordinatesFactory(versionProvider);

        List<MavenArtifactCoordinates> mavenArtifacts = new ArrayList<>();

        mavenArtifacts.add(mavenArtifactCoordinatesFactory.create(mavenPublication, normalisedPublication.getPomArtifact()));
        mavenArtifacts.add(mavenArtifactCoordinatesFactory.create(mavenPublication, normalisedPublication.getMainArtifact()));
        Set<org.gradle.api.publish.maven.MavenArtifact> additionalArtifacts = normalisedPublication.getAdditionalArtifacts();
        for (org.gradle.api.publish.maven.MavenArtifact additionalArtifact : additionalArtifacts) {
            mavenArtifacts.add(mavenArtifactCoordinatesFactory.create(mavenPublication, additionalArtifact));
        }

        return mavenArtifacts;
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

    @Override
    public ArtifactRepositoryDesc getArtifactRepositoryDesc() {
        return new MavenArtifactRepositoryDesc(mavenArtifactRepository);
    }
}
