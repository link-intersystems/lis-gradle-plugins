package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.ArtifactPublicationProviders;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.Publication;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class VerifyPublicationTaskRegistrar {

    private Logger logger = LoggerFactory.getLogger(VerifyPublicationTaskRegistrar.class);

    private final Project project;

    public VerifyPublicationTaskRegistrar(Project project) {
        this.project = project;
    }

    public void registerTask(VerifyPublication verifyPublication) {
        VerifyPublicationConfig verifyPublicationConfig = new VerifyPublicationConfig(verifyPublication);

        Publication publication = getPublication(project, verifyPublication);
        RepositoryHandler repositories = getRepositories(project, verifyPublication);
        ArtifactPublicationProviders providers = ArtifactPublicationProviders.get();

        repositories.all(repository -> {
            Optional<ArtifactPublication> artifactPublicationOptional = providers.createArtifactPublication(publication, repository, verifyPublicationConfig.getVersionProvider());
            artifactPublicationOptional.ifPresentOrElse(provider -> registerTask(provider, verifyPublicationConfig), () -> {
                logger.error("Can not create an ArtifactPublication for publication {} in repository {}. No ArtifactPublicationProvider available: {}", publication, repository, providers);
            });
        });
    }

    private RepositoryHandler getRepositories(Project project, VerifyPublication verifyPublication) {
        RepositoryHandler repositories = verifyPublication.getVerifyRepositories();

        if (repositories.isEmpty()) {
            repositories = project.getExtensions().getByType(PublishingExtension.class).getRepositories();
        }

        return repositories;
    }

    private Publication getPublication(Project project, VerifyPublication verifyPublication) {
        Publication publication = verifyPublication.getPublication();
        if (publication == null) {
            publication = project.getExtensions().getByType(PublishingExtension.class).getPublications().getByName(verifyPublication.getName());
        }
        return publication;
    }

    @SuppressWarnings("rawtypes")
    public void registerTask(ArtifactPublication artifactPublication, VerifyPublicationConfig verifyPublicationConfig) {

        String publicationName = artifactPublication.getArtifactName();
        String repositoryName = artifactPublication.getArtifactRepositoryName();
        String taskName = "verify" + capitalize(publicationName) + "PublicationTo" + capitalize(repositoryName) + "Repository";

        TaskContainer tasks = project.getTasks();
        TaskProvider<VerifyPublicationTask> publishingCheckTaskTaskProvider = tasks.register(taskName, VerifyPublicationTask.class, artifactPublication, verifyPublicationConfig);
        publishingCheckTaskTaskProvider.configure(task -> {
            task.setGroup("publications");
        });
    }

    public static String capitalize(final String str) {
        int strLen = str.length();

        int firstCodepoint = str.codePointAt(0);
        int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        int[] newCodePoints = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first code point
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codePoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codePoint; // copy the remaining ones
            inOffset += Character.charCount(codePoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }
}
