package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.Artifact;
import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactRepository;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PublicationCheckerTask extends DefaultTask {

    private final ArtifactPublication artifactPublication;
    private Logger logger = LoggerFactory.getLogger(PublicationCheckerTask.class);

    @Inject
    public PublicationCheckerTask(ArtifactPublication artifactPublication) {
        this.artifactPublication = artifactPublication;
    }

    @TaskAction
    public void checkPublishing() {
        Artifact artifact = artifactPublication.getArtifact();
        ArtifactRepository artifactRepository = artifactPublication.getArtifactRepository();


        if (artifactRepository.exists(artifact)) {
            logger.info("FOUND: {} ", artifact);
            throw new GradleException("Artifact \"" + artifact + "\" already exists in repository " + artifactRepository);
        } else {
            logger.info("MISSING: {} ", artifact);
        }
    }
}
