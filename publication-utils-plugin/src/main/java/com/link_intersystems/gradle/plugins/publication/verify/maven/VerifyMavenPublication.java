package com.link_intersystems.gradle.plugins.publication.verify.maven;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublication;
import com.link_intersystems.gradle.publication.maven.MavenArtifactCoordinates;

public interface VerifyMavenPublication extends VerifyPublication {

    void setArtifactFilter(ArtifactFilter<MavenArtifactCoordinates> artifactFilter);

    ArtifactFilter<MavenArtifactCoordinates> getArtifactFilter();
}
