package com.link_intersystems.gradle.publication.plugins.verify;


import org.gradle.api.artifacts.repositories.ArtifactRepository;

import java.util.List;

public interface VerifyRepositoryHandler {
    List<ArtifactRepository> getArtifactRepositories();
}
