package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.plugins.publication.VersionProvider;
import org.gradle.api.Action;
import org.gradle.api.Named;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.Publication;

public interface VerifyPublication extends Named {

    Publication getPublication();

    RepositoryHandler getVerifyRepositories();

    void verifyRepositories(Action<? super RepositoryHandler> configure);

    void setResultHandler(VerifyPublicationResultHandler verifyPublicationResultHandler);

    VerifyPublicationResultHandler getResultHandler();

    ArtifactFilter<?> getArtifactFilter();

    VersionProvider getVersionProvider();

    void setVersionProvider(VersionProvider versionProvider);

}
