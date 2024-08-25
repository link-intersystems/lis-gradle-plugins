package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class VerifyPublicationTaskRegistrar {

    private final TaskContainer tasks;
    private final VerifyPublicationExtension verifyPublicationExtension;

    public VerifyPublicationTaskRegistrar(TaskContainer tasks, VerifyPublicationExtension verifyPublicationExtension) {
        this.tasks = tasks;
        this.verifyPublicationExtension = verifyPublicationExtension;
    }

    @SuppressWarnings("rawtypes")
    public void registerTask(ArtifactPublication artifactPublication) {
        VerifyPublicationConfig verifyPublicationConfig = getConfig();

        String publicationName = artifactPublication.getArtifactName();
        String repositoryName = artifactPublication.getArtifactRepositoryName();
        String taskName = "verify" + capitalize(publicationName) + "PublicationTo" + capitalize(repositoryName) + "Repository";

        TaskProvider<VerifyPublicationTask> publishingCheckTaskTaskProvider = tasks.register(taskName, VerifyPublicationTask.class, artifactPublication, verifyPublicationConfig);
        publishingCheckTaskTaskProvider.configure(task -> {
            task.setGroup("publications");
        });
    }

    private VerifyPublicationConfig getConfig() {
        return new VerifyPublicationConfig() {
            @Override
            public VerifyMode getMode() {
                VerifyMode verifyMode = verifyPublicationExtension.getMode();
                if (verifyMode == null) {
                    verifyMode = VerifyModes.NONE_EXISTS;
                }
                return verifyMode;
            }
        };
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
