package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactPublicationProvider;
import com.link_intersystems.gradle.publication.VersionProvider;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.publish.Publication;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;

public class MavenArtifactPublicationProvider extends ArtifactPublicationProvider {

    @Override
    public ArtifactPublication tryCreateArtifactPublication(Publication publication, ArtifactRepository repository, VersionProvider versionProvider) {
        if (supports(publication, repository)) {
            return MavenArtifactPublication.of((MavenPublicationInternal) publication, (MavenArtifactRepository) repository, versionProvider);
        }
        return null;
    }

    private boolean supports(Publication publication, ArtifactRepository repository) {
        return publication instanceof MavenPublication && repository instanceof MavenArtifactRepository;
    }
}
