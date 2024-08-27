package com.link_intersystems.gradle.plugins.publication.utils;

import com.link_intersystems.gradle.plugins.publication.verify.DefaultVerifyPublicationContainer;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublication;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationContainer;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationTaskRegistrar;
import com.link_intersystems.gradle.plugins.publication.verify.maven.VerifyMavenPublication;
import com.link_intersystems.gradle.plugins.publication.verify.maven.VerifyMavenPublicationFactory;
import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactPublicationProvider;
import com.link_intersystems.gradle.publication.ArtifactPublicationProviders;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.artifacts.BaseRepositoryFactory;
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler;
import org.gradle.api.publish.Publication;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.internal.reflect.Instantiator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;

public class PublicationUtilsPlugin implements Plugin<Project> {

    private final DefaultRepositoryHandler repositoryHandler;
    private Logger logger = LoggerFactory.getLogger(PublicationUtilsPlugin.class);

    private final Instantiator instantiator;
    private final CollectionCallbackActionDecorator collectionCallbackActionDecorator;
    private final BaseRepositoryFactory repositoryFactory;

    @Inject
    public PublicationUtilsPlugin(Instantiator instantiator, CollectionCallbackActionDecorator collectionCallbackActionDecorator, BaseRepositoryFactory repositoryFactory) {
        this.instantiator = instantiator;
        this.collectionCallbackActionDecorator = collectionCallbackActionDecorator;
        this.repositoryFactory = repositoryFactory;
        repositoryHandler = new DefaultRepositoryHandler(repositoryFactory, instantiator, collectionCallbackActionDecorator);
    }

    public void apply(Project p) {
        VerifyPublicationContainer verifyPublications = instantiator.newInstance(DefaultVerifyPublicationContainer.class, instantiator, collectionCallbackActionDecorator);
        verifyPublications.registerFactory(VerifyMavenPublication.class, new VerifyMavenPublicationFactory(repositoryHandler));
        p.getExtensions().create(PublicationUtilsExtension.class, PublicationUtilsExtension.NAME, DefaultPublicationUtilsExtension.class, verifyPublications);

        p.afterEvaluate(this::registerPublicationCheckerTasks);
    }

    @Inject
    protected BaseRepositoryFactory getBaseRepositoryFactory() {
        throw new UnsupportedOperationException();
    }


    private void registerPublicationCheckerTasks(Project project) {
        PublicationUtilsExtension publicationUtilsExtension = project.getExtensions().findByType(PublicationUtilsExtension.class);

        ArtifactPublicationProviders providers = ArtifactPublicationProvider.getProviders();

        VerifyPublicationTaskRegistrar taskRegistrar = new VerifyPublicationTaskRegistrar(project.getTasks());

        VerifyPublicationContainer verify = publicationUtilsExtension.getVerify();
        SortedMap<String, VerifyPublication> verifyPublications = verify.getAsMap();
        for (Map.Entry<String, VerifyPublication> verifyPublicationEntry : verifyPublications.entrySet()) {
            VerifyPublication verifyPublication = verifyPublicationEntry.getValue();
            Publication publication = getPublication(project, verifyPublication);

            RepositoryHandler repositories = verifyPublication.getVerifyRepositories();

            repositories.all(repository -> {
                Optional<ArtifactPublication> provider = providers.createArtifactPublication(publication, repository);
                provider.ifPresentOrElse(ap -> taskRegistrar.registerTask(ap, verifyPublication), () -> {
                    logger.error("Can not create an ArtifactPublication for publication {} in repository {}. No ArtifactPublicationProvider available: {}", publication, repository, providers);
                });
            });
        }
    }

    private static @NotNull Publication getPublication(Project project, VerifyPublication verifyPublication) {
        Publication publication = verifyPublication.getPublication();
        if (publication == null) {
            publication = project.getExtensions().getByType(PublishingExtension.class).getPublications().getByName(verifyPublication.getName());
        }
        return publication;
    }
}
