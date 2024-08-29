package com.link_intersystems.gradle.publication.plugins.verify;

import com.link_intersystems.gradle.publication.plugins.PublicationServices;
import com.link_intersystems.gradle.publication.plugins.PublicationUtil;
import com.link_intersystems.gradle.publication.plugins.PublicationUtilsExtension;
import com.link_intersystems.gradle.publication.plugins.verify.maven.VerifyMavenPublication;
import com.link_intersystems.gradle.publication.plugins.verify.maven.VerifyMavenPublicationFactory;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.publish.PublishingExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.SortedMap;

/**
 * A publication util that can verify if artifacts exist in remote repositories.
 */
public class VerifyPublicationUtil implements PublicationUtil {

    private Logger logger = LoggerFactory.getLogger(VerifyPublicationUtil.class);

    @Override
    public void apply(Project project, PublicationServices publicationServices) {
        ExtensionContainer utilsExtensionContainer = publicationServices.getUtilsExtensionContainer();
        VerifyPublicationContainer verifyPublicationContainer = utilsExtensionContainer.create(VerifyPublicationContainer.class, "verify", DefaultVerifyPublicationContainer.class);

        RepositoryHandler repositoryHandler = publicationServices.createRepositoryHandler();
        verifyPublicationContainer.registerFactory(VerifyMavenPublication.class, new VerifyMavenPublicationFactory(repositoryHandler));

        project.afterEvaluate(this::registerVerifyPublicationTasks);
    }

    private void registerVerifyPublicationTasks(Project project) {
        PublishingExtension publishingExtension = project.getExtensions().findByType(PublishingExtension.class);
        if (publishingExtension == null) {
            logger.error("No publishing extension found. Verify publication tasks will not be added to build. Add a publishing plugin like `maven-publish` to the project.");
            return;
        }

        PublicationUtilsExtension publicationUtilsExtension = project.getExtensions().findByType(PublicationUtilsExtension.class);

        VerifyPublicationTaskRegistrar taskRegistrar = new VerifyPublicationTaskRegistrar(project);

        VerifyPublicationContainer verifyPublicationContainer = publicationUtilsExtension.getExtensions().getByType(VerifyPublicationContainer.class);
        SortedMap<String, VerifyPublication> verifyPublications = verifyPublicationContainer.getAsMap();

        for (Map.Entry<String, VerifyPublication> verifyPublicationEntry : verifyPublications.entrySet()) {
            VerifyPublication verifyPublication = verifyPublicationEntry.getValue();
            taskRegistrar.registerTask(verifyPublication);
        }
    }
}
