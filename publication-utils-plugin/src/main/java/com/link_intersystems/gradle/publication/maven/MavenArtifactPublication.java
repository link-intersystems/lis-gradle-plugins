package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactRepository;
import com.link_intersystems.gradle.publication.VersionProvider;
import com.link_intersystems.gradle.publication.plugins.ArtifactRepositoryDesc;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.maven.internal.publisher.MavenNormalizedPublication;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MavenArtifactPublication implements ArtifactPublication {

    public static MavenArtifactPublication of(MavenPublicationInternal publication, org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository, VersionProvider versionProvider) {
        MavenNormalizedPublication normalisedPublication = publication.asNormalisedPublication();
        MavenArtifactCoordinatesFactory mavenArtifactCoordinatesFactory = new MavenArtifactCoordinatesFactory(versionProvider);

        List<MavenArtifactCoordinates> mavenArtifacts = new ArrayList<>();

        mavenArtifacts.add(mavenArtifactCoordinatesFactory.create(publication, normalisedPublication.getPomArtifact()));
        mavenArtifacts.add(mavenArtifactCoordinatesFactory.create(publication, normalisedPublication.getMainArtifact()));
        Set<org.gradle.api.publish.maven.MavenArtifact> additionalArtifacts = normalisedPublication.getAdditionalArtifacts();
        for (org.gradle.api.publish.maven.MavenArtifact additionalArtifact : additionalArtifacts) {
            mavenArtifacts.add(mavenArtifactCoordinatesFactory.create(publication, additionalArtifact));
        }

        return new MavenArtifactPublication(MavenArtifactRepositoryDesc.of(mavenArtifactRepository), new MavenArtifactRepository(mavenArtifactRepository), mavenArtifacts, publication.getName());
    }


    private final ArtifactRepositoryDesc artifactRepositoryDesc;
    private final ArtifactRepository artifactRepository;
    private final List<MavenArtifactCoordinates> mavenArtifactCoordinates;
    private final String publicationName;


    public MavenArtifactPublication(ArtifactRepositoryDesc artifactRepositoryDesc, ArtifactRepository artifactRepository, List<MavenArtifactCoordinates> mavenArtifactCoordinates, String publicationName) {
        this.artifactRepositoryDesc = artifactRepositoryDesc;
        this.artifactRepository = artifactRepository;
        this.mavenArtifactCoordinates = mavenArtifactCoordinates;
        this.publicationName = publicationName;
    }

    @Override
    public List<? extends ArtifactCoordinates> getArtifactCoordinates() {


        return mavenArtifactCoordinates;
    }

    @Override
    public ArtifactRepository getArtifactRepository() {
        return artifactRepository;
    }

    @Override
    public String getPublicationName() {
        return publicationName;
    }

    @Override
    public ArtifactRepositoryDesc getArtifactRepositoryDesc() {
        return artifactRepositoryDesc;
    }
}
