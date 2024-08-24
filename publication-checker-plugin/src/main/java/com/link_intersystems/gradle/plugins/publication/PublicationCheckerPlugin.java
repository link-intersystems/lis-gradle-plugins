package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactPublicationProvider;
import com.link_intersystems.gradle.publication.ArtifactPublicationProviders;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PublicationCheckerPlugin implements Plugin<Project> {

    private Logger logger = LoggerFactory.getLogger(PublicationCheckerPlugin.class);

    public void apply(Project p) {
        p.getExtensions().create(PublicationCheckerExtension.class, PublicationCheckerExtension.NAME, DefaultPublicationCheckerExtension.class);

        p.afterEvaluate(this::registerPublicationCheckerTasks);
    }

    private void registerPublicationCheckerTasks(Project project) {
        PublicationCheckerExtension checkerExtension = project.getExtensions().findByType(PublicationCheckerExtension.class);
        PublishingExtension publishingExtension = project.getExtensions().findByType(PublishingExtension.class);

        ArtifactPublicationProviders providers = ArtifactPublicationProvider.getProviders();

        PublicationCheckerTaskRegistrar taskRegistrar = new PublicationCheckerTaskRegistrar(project);

        PublicationContainer publications = publishingExtension.getPublications();
        if (publications != null) {
            publications.all(publication -> {
                publishingExtension.getRepositories().all(repository -> {
                    Optional<ArtifactPublication> provider = providers.createArtifactPublication(publication, repository);
                    provider.ifPresentOrElse(taskRegistrar::registerPublicationCheckerTask, () -> {
                        logger.debug("Can not create an ArtifactPublication for publication {} in repository {}. No ArtifactPublicationProvider available: {}", publication, repository, providers);
                    });
                });
            });
        }
    }
}
