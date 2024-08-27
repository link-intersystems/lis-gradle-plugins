package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import org.gradle.api.Action;
import org.gradle.api.Named;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.Publication;

public interface VerifyPublication extends Named {
    void setPublication(Publication publication);

    Publication getPublication();

    RepositoryHandler getVerifyRepositories();

    void verifyRepositories(Action<? super RepositoryHandler> configure);

    void setMode(VerifyMode verifyMode);

    VerifyMode getMode();

    ArtifactFilter<?> getArtifactFilter();

}
