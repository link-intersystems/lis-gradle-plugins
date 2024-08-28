package com.link_intersystems.gradle.publication.plugins.verify;

import com.link_intersystems.gradle.publication.plugins.PublicationServices;
import com.link_intersystems.gradle.publication.plugins.PublicationUtil;
import com.link_intersystems.gradle.publication.plugins.PublicationUtilsExtension;
import com.link_intersystems.gradle.publication.plugins.verify.maven.VerifyMavenPublication;
import com.link_intersystems.gradle.publication.plugins.verify.maven.VerifyMavenPublicationFactory;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.ExtensionContainer;

import java.util.Map;
import java.util.SortedMap;

public class VerifyPublicationUtil implements PublicationUtil {

    @Override
    public void apply(Project project, PublicationServices publicationServices) {
        ExtensionContainer utilsExtensionContainer = publicationServices.getUtilsExtensionContainer();
        VerifyPublicationContainer verifyPublicationContainer = utilsExtensionContainer.create(VerifyPublicationContainer.class, "verify", DefaultVerifyPublicationContainer.class);

        RepositoryHandler repositoryHandler = publicationServices.createRepositoryHandler();
        verifyPublicationContainer.registerFactory(VerifyMavenPublication.class, new VerifyMavenPublicationFactory(repositoryHandler));

        project.afterEvaluate(this::registerPublicationCheckerTasks);
    }

    private void registerPublicationCheckerTasks(Project project) {
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
