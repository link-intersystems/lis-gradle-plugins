package com.link_intersystems.gradle.publication;

import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.publish.Publication;

public abstract class ArtifactPublicationProvider {

    public abstract ArtifactPublication tryCreateArtifactPublication(Publication publication, ArtifactRepository repository, VersionProvider versionProvider);
}
