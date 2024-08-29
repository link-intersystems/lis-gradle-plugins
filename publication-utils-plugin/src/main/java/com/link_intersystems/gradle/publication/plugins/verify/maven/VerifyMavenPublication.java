package com.link_intersystems.gradle.publication.plugins.verify.maven;

import com.link_intersystems.gradle.publication.maven.MavenArtifactCoordinates;
import com.link_intersystems.gradle.publication.plugins.ArtifactFilter;
import com.link_intersystems.gradle.publication.plugins.verify.VerifyPublication;
import org.gradle.api.Action;

import java.util.List;

public interface VerifyMavenPublication extends VerifyPublication {

    MavenVerifyRepositoryHandler getVerifyRepositories();

    void verifyRepositories(Action<? super MavenVerifyRepositoryHandler> configure);


    void setArtifactFilter(ArtifactFilter<MavenArtifactCoordinates> artifactFilter);

    ArtifactFilter<MavenArtifactCoordinates> getArtifactFilter();
}
