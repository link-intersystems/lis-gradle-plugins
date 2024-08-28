package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import com.link_intersystems.gradle.publication.VersionProvider;
import com.link_intersystems.gradle.publication.VersionProviders;

public class VerifyPublicationConfig  {
    private final VerifyPublication verifyPublication;

    public VerifyPublicationConfig(VerifyPublication verifyPublication) {
        this.verifyPublication = verifyPublication;
    }

    public VerifyPublicationResultHandler getMode() {
        VerifyPublicationResultHandler verifyPublicationResultHandler = verifyPublication.getResultHandler();
        if (verifyPublicationResultHandler == null) {
            verifyPublicationResultHandler = VerifyPublicationResultHandlers.NONE_EXISTS;
        }
        return verifyPublicationResultHandler;
    }

    @SuppressWarnings("unchecked")
    public ArtifactFilter<ArtifactCoordinates> getFilter() {
        ArtifactFilter<ArtifactCoordinates> artifactFilter = (ArtifactFilter<ArtifactCoordinates>) verifyPublication.getArtifactFilter();
        return artifactFilter == null ? ArtifactFilter.ALL : artifactFilter;
    }

    public VersionProvider getVersionProvider() {
        VersionProvider versionProvider = verifyPublication.getVersionProvider();

        if (versionProvider == null) {
            versionProvider = VersionProviders.CURRENT_VERSION;
        }

        return versionProvider;
    }
}
