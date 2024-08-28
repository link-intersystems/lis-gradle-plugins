package com.link_intersystems.gradle.plugins.publication.verify;


import org.gradle.api.artifacts.repositories.ArtifactRepository;

import java.util.List;

public interface VerifyRepositoryHandler {
    List<ArtifactRepository> getArtifactRepositories();
}
