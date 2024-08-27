package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;

public interface VerifyPublicationConfig {

    public VerifyMode getMode();

    public ArtifactFilter<ArtifactCoordinates> getFilter();
}
