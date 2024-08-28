package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import com.link_intersystems.gradle.publication.VersionProvider;
import com.link_intersystems.gradle.publication.VersionProviders;

public class DefaultVerifyPublicationConfig implements VerifyPublicationConfig {
    private final VerifyPublication verifyPublication;

    public DefaultVerifyPublicationConfig(VerifyPublication verifyPublication) {
        this.verifyPublication = verifyPublication;
    }

    @Override
    public VerifyPublicationResultHandler getMode() {
        VerifyPublicationResultHandler verifyPublicationResultHandler = verifyPublication.getResultHandler();
        if (verifyPublicationResultHandler == null) {
            verifyPublicationResultHandler = VerifyPublicationResultHandlers.NONE_EXISTS;
        }
        return verifyPublicationResultHandler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArtifactFilter<ArtifactCoordinates> getFilter() {
        ArtifactFilter<ArtifactCoordinates> artifactFilter = (ArtifactFilter<ArtifactCoordinates>) verifyPublication.getArtifactFilter();
        return artifactFilter == null ? ArtifactFilter.ALL : artifactFilter;
    }

    @Override
    public VersionProvider getVersionProvider() {
        VersionProvider versionProvider = verifyPublication.getVersionProvider();

        if (versionProvider == null) {
            versionProvider = VersionProviders.CURRENT_VERSION;
        }

        return versionProvider;
    }
}
