package com.link_intersystems.gradle.publication.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.artifacts.BaseRepositoryFactory;
import org.gradle.api.internal.artifacts.dsl.DefaultRepositoryHandler;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.internal.reflect.Instantiator;

import javax.inject.Inject;

public class PublicationUtilsPlugin implements Plugin<Project> {

    class DefaultPublicationServices implements PublicationServices {

        private final Project project;

        DefaultPublicationServices(Project project) {
            this.project = project;
        }

        @Override
        public ExtensionContainer getUtilsExtensionContainer() {
            return project.getExtensions().getByType(PublicationUtilsExtension.class).getExtensions();
        }

        @Override
        public RepositoryHandler createRepositoryHandler() {
            return new DefaultRepositoryHandler(repositoryFactory, instantiator, collectionCallbackActionDecorator);
        }
    }

    private final Instantiator instantiator;
    private final BaseRepositoryFactory repositoryFactory;
    private final CollectionCallbackActionDecorator collectionCallbackActionDecorator;

    @Inject
    public PublicationUtilsPlugin(Instantiator instantiator, BaseRepositoryFactory repositoryFactory, CollectionCallbackActionDecorator collectionCallbackActionDecorator) {
        this.instantiator = instantiator;
        this.repositoryFactory = repositoryFactory;
        this.collectionCallbackActionDecorator = collectionCallbackActionDecorator;
    }

    public void apply(Project p) {
        p.getExtensions().create(PublicationUtilsExtension.class, PublicationUtilsExtension.NAME, DefaultPublicationUtilsExtension.class);

        PublicationUtils publicationUtils = PublicationUtils.get();
        publicationUtils.apply(p, new DefaultPublicationServices(p));
    }


}
