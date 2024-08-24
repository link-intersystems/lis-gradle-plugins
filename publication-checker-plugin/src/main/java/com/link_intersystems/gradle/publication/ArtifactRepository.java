package com.link_intersystems.gradle.publication;

public interface ArtifactRepository {
    boolean exists(Artifact artifact);

//    Metadata getMetaData(MavenArtifact artifact);
}
