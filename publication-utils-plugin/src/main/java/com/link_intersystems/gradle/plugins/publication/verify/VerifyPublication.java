package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.publication.VersionProvider;
import org.gradle.api.Named;
import org.gradle.api.publish.Publication;

public interface VerifyPublication extends Named {

    Publication getPublication();

    void setResultHandler(VerifyPublicationResultHandler verifyPublicationResultHandler);

    VerifyPublicationResultHandler getResultHandler();

    ArtifactFilter<?> getArtifactFilter();

    VersionProvider getVersionProvider();

    void setVersionProvider(VersionProvider versionProvider);

    VerifyRepositoryHandler getVerifyRepositories();
}
