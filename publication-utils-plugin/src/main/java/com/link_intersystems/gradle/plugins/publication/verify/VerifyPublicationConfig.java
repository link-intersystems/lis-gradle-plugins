package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import com.link_intersystems.gradle.publication.VersionProvider;

public interface VerifyPublicationConfig {

    public VerifyPublicationResultHandler getMode();

    public ArtifactFilter<ArtifactCoordinates> getFilter();

    public VersionProvider getVersionProvider();
}
