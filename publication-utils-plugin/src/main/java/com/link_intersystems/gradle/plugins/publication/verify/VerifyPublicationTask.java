package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.ArtifactRepositoryDesc;
import com.link_intersystems.gradle.plugins.publication.VerifyPublicationArtifactResult;
import com.link_intersystems.gradle.plugins.publication.VerifyPublicationResult;
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

        for (ArtifactCoordinates artifactCoordinates : artifacts) {
            boolean exists = artifactRepository.exists(artifactCoordinates);
            verifyResults.add(new VerifyPublicationArtifactResult(artifactCoordinates, exists));
        }

        VerifyMode verifyMode = verifyPublicationConfig.getMode();
        verifyMode.handle(new VerifyPublicationResult(repositoryDesc, verifyResults));
    }
}
