package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class VerifyPublicationTaskRegistrar {

    private final TaskContainer tasks;
    private final VerifyPublicationConfig config;

    public VerifyPublicationTaskRegistrar(TaskContainer tasks, VerifyPublicationConfig config) {
        this.tasks = tasks;
        this.config = config;
    }

    @SuppressWarnings("rawtypes")
    public void registerTask(ArtifactPublication artifactPublication) {

        String publicationName = artifactPublication.getArtifactName();
        String repositoryName = artifactPublication.getArtifactRepositoryName();
        String taskName = "verify" + capitalize(publicationName) + "PublicationTo" + capitalize(repositoryName) + "Repository";

        TaskProvider<VerifyPublicationTask> publishingCheckTaskTaskProvider = tasks.register(taskName, VerifyPublicationTask.class, artifactPublication, config);
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
