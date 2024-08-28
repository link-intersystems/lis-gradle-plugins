package com.link_intersystems.gradle.publication.plugins.verify.maven;

import com.link_intersystems.gradle.publication.plugins.ArtifactFilter;
import com.link_intersystems.gradle.publication.plugins.verify.VerifyPublication;
import com.link_intersystems.gradle.publication.maven.MavenArtifactCoordinates;
import org.gradle.api.Action;
import org.gradle.api.publish.maven.MavenPublication;

public interface VerifyMavenPublication extends VerifyPublication {

    void setPublication(MavenPublication mavenPublication);

    MavenVerifyRepositoryHandler getVerifyRepositories();

    void verifyRepositories(Action<? super MavenVerifyRepositoryHandler> configure);


    void setArtifactFilter(ArtifactFilter<MavenArtifactCoordinates> artifactFilter);

    ArtifactFilter<MavenArtifactCoordinates> getArtifactFilter();
}
