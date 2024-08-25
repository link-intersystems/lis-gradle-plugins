package com.link_intersystems.gradle.plugins.publication.utils;

import com.link_intersystems.gradle.plugins.publication.AllPublicationFilter;
import com.link_intersystems.gradle.plugins.publication.PublicationFilter;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationExtension;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationTaskRegistrar;
import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactPublicationProvider;
import com.link_intersystems.gradle.publication.ArtifactPublicationProviders;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.artifacts.BaseRepositoryFactory;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class PublicationUtilsPlugin implements Plugin<Project> {

    private Logger logger = LoggerFactory.getLogger(PublicationUtilsPlugin.class);

    public void apply(Project p) {
        p.getExtensions().create(PublicationUtilsExtension.class, PublicationUtilsExtension.NAME, PublicationUtilsExtension.class);

        p.afterEvaluate(this::registerPublicationCheckerTasks);
    }

    @Inject
    protected BaseRepositoryFactory getBaseRepositoryFactory() {
        throw new UnsupportedOperationException();
    }


    private void registerPublicationCheckerTasks(Project project) {
        PublicationUtilsExtension publicationUtilsExtension = project.getExtensions().findByType(PublicationUtilsExtension.class);
        PublishingExtension publishingExtension = project.getExtensions().findByType(PublishingExtension.class);

        ArtifactPublicationProviders providers = ArtifactPublicationProvider.getProviders();

        VerifyPublicationExtension verify = publicationUtilsExtension.getVerify();
        VerifyPublicationTaskRegistrar taskRegistrar = new VerifyPublicationTaskRegistrar(project.getTasks(), verify);

        PublicationFilter publicationFilter = verify.getPublicationFilter() == null ? new AllPublicationFilter() : verify.getPublicationFilter();

        PublicationContainer publications = publishingExtension.getPublications();
        if (publications != null) {
            publications.all(publication -> {
                if (publicationFilter.accept(publication)) {
                    publishingExtension.getRepositories().all(repository -> {
                        Optional<ArtifactPublication> provider = providers.createArtifactPublication(publication, repository);
                        provider.ifPresentOrElse(taskRegistrar::registerTask, () -> {
                            logger.debug("Can not create an ArtifactPublication for publication {} in repository {}. No ArtifactPublicationProvider available: {}", publication, repository, providers);
                        });
                    });
                }
            });
        }
    }
}
