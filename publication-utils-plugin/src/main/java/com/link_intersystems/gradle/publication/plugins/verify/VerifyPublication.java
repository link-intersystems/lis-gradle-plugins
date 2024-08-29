package com.link_intersystems.gradle.publication.plugins.verify;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.VersionProvider;
import com.link_intersystems.gradle.publication.plugins.ArtifactFilter;
import org.gradle.api.Named;
import org.gradle.api.Project;

import java.util.List;

public interface VerifyPublication extends Named {

    void setArtifacts(List<String> coords);

    List<String> getArtifacts();

    void setResultHandler(VerifyPublicationResultHandler verifyPublicationResultHandler);

    VerifyPublicationResultHandler getResultHandler();

    ArtifactFilter<?> getArtifactFilter();

    VersionProvider getVersionProvider();

    void setVersionProvider(VersionProvider versionProvider);

    VerifyRepositoryHandler getVerifyRepositories();

    public List<ArtifactPublication> getArtifactPublications(Project project);

}
