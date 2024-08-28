package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactFilter;
import com.link_intersystems.gradle.plugins.publication.ArtifactRepositoryDesc;
import com.link_intersystems.gradle.publication.ArtifactCoordinates;
import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactRepository;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class VerifyPublicationTask extends DefaultTask {

    private final ArtifactPublication artifactPublication;
    private final VerifyPublicationConfig verifyPublicationConfig;

    @Inject
    public VerifyPublicationTask(ArtifactPublication artifactPublication, VerifyPublicationConfig verifyPublicationConfig) {
        this.verifyPublicationConfig = verifyPublicationConfig;
        this.artifactPublication = artifactPublication;
    }

    @TaskAction
    public void checkPublishing() {
        List<? extends ArtifactCoordinates> artifacts = artifactPublication.getArtifacts();
        ArtifactRepository artifactRepository = artifactPublication.getArtifactRepository();
        ArtifactRepositoryDesc repositoryDesc = artifactPublication.getArtifactRepositoryDesc();

        List<VerifyPublicationArtifactResult> verifyResults = new ArrayList<>();

        ArtifactFilter<ArtifactCoordinates> artifactFilter = verifyPublicationConfig.getFilter();

        for (ArtifactCoordinates artifactCoordinates : artifacts) {
            if (artifactFilter.accept(artifactCoordinates)) {
                boolean exists = artifactRepository.exists(artifactCoordinates);
                verifyResults.add(new VerifyPublicationArtifactResult(artifactCoordinates, exists));
            } else {
                getLogger().debug("Skip artifact '{}', because the artifact filter '{}' does not accept it", artifactCoordinates, artifactFilter);
            }
        }

        VerifyPublicationResultHandler verifyPublicationResultHandler = verifyPublicationConfig.getMode();
        verifyPublicationResultHandler.handle(new VerifyPublicationResult(repositoryDesc, verifyResults));
    }
}
