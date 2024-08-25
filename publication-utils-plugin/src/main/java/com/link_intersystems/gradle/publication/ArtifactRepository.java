package com.link_intersystems.gradle.publication;

public interface ArtifactRepository {
    boolean exists(ArtifactCoordinates artifactCoordinates);

//    Metadata getMetaData(MavenArtifact artifact);
}
