package com.link_intersystems.gradle.publication.plugins.verify;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VerifyPublicationTaskRegistrar {

    private Logger logger = LoggerFactory.getLogger(VerifyPublicationTaskRegistrar.class);

    private final Project project;

    public VerifyPublicationTaskRegistrar(Project project) {
        this.project = project;
    }

    public void registerTask(VerifyPublication verifyPublication) {
        VerifyPublicationConfig verifyPublicationConfig = new VerifyPublicationConfig(verifyPublication);

        List<ArtifactPublication> artifactPublications = verifyPublication.getArtifactPublications(project);

        for (ArtifactPublication artifactPublication : artifactPublications) {
            registerTask(artifactPublication, verifyPublicationConfig);
        }
    }


    @SuppressWarnings("rawtypes")
    public void registerTask(ArtifactPublication artifactPublication, VerifyPublicationConfig verifyPublicationConfig) {

        String publicationName = artifactPublication.getPublicationName();
        String repositoryName = artifactPublication.getArtifactRepositoryDesc().getName();
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
