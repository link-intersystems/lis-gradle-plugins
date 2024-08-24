package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.Artifact;
import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactRepository;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PublicationCheckerTask extends DefaultTask {

    private final ArtifactPublication artifactPublication;
    private final PublicationCheckerConfig publicationCheckerConfig;

    @Inject
    public PublicationCheckerTask(ArtifactPublication artifactPublication, PublicationCheckerConfig publicationCheckerConfig) {
        this.publicationCheckerConfig = publicationCheckerConfig;
        this.artifactPublication = artifactPublication;
    }

    @TaskAction
    public void checkPublishing() {
        List<? extends Artifact> artifacts = artifactPublication.getArtifacts();
        ArtifactRepository artifactRepository = artifactPublication.getArtifactRepository();
        ArtifactRepositoryDesc repositoryDecs = artifactPublication.getArtifactRepositoryDesc();

        List<ArtifactCheckResult> artifactCheckResults = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            boolean exists = artifactRepository.exists(artifact);
            artifactCheckResults.add(new ArtifactCheckResult(artifact, exists));
        }

        CheckResultStrategy checkResultStrategy = publicationCheckerConfig.getCheckResultStrategy();
        checkResultStrategy.handle(new PublicationCheckResult(artifactCheckResults, repositoryDecs));
    }
}
