package com.link_intersystems.gradle.plugins.publication.utils;

import com.link_intersystems.gradle.plugins.publication.verify.DefaultVerifyPublicationContainer;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublication;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationContainer;
import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationTaskRegistrar;
import com.link_intersystems.gradle.plugins.publication.verify.maven.VerifyMavenPublication;
import com.link_intersystems.gradle.plugins.publication.verify.maven.VerifyMavenPublicationFactory;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.artifacts.BaseRepositoryFactory;
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler;
import org.gradle.internal.reflect.Instantiator;

import javax.inject.Inject;
import java.util.Map;
import java.util.SortedMap;

public class PublicationUtilsPlugin implements Plugin<Project> {

    private final DefaultRepositoryHandler repositoryHandler;

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


    private void registerPublicationCheckerTasks(Project project) {
        PublicationUtilsExtension publicationUtilsExtension = project.getExtensions().findByType(PublicationUtilsExtension.class);

        VerifyPublicationTaskRegistrar taskRegistrar = new VerifyPublicationTaskRegistrar(project);

        VerifyPublicationContainer verifyPublicationContainer = publicationUtilsExtension.getVerify();
        SortedMap<String, VerifyPublication> verifyPublications = verifyPublicationContainer.getAsMap();

        for (Map.Entry<String, VerifyPublication> verifyPublicationEntry : verifyPublications.entrySet()) {
            VerifyPublication verifyPublication = verifyPublicationEntry.getValue();
            taskRegistrar.registerTask(verifyPublication);
        }
    }
}
