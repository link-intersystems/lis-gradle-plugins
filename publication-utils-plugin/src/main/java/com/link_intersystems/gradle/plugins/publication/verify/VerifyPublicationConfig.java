package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.plugins.publication.VersionProvider;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;

public interface VerifyPublicationConfig {

    public VerifyPublicationResultHandler getMode();

    public ArtifactFilter<ArtifactCoordinates> getFilter();

    public VersionProvider getVersionProvider();
}
