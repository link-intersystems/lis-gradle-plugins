package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.ArtifactPublication;
import org.gradle.api.Project;
import org.gradle.api.publish.plugins.PublishingPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

class PublicationCheckerTaskRegistrar {

    private Project project;

    public PublicationCheckerTaskRegistrar(Project project) {
        this.project = project;
    }

    @SuppressWarnings("rawtypes")
    void registerPublicationCheckerTask(ArtifactPublication artifactPublication) {
        PublicationCheckerConfig publicationCheckerConfig = getConfig();
        final TaskContainer tasks = project.getTasks();

        final String publicationName = artifactPublication.getArtifactName();
        final String repositoryName = artifactPublication.getArtifactRepositoryName();
        final String taskName = "check" + capitalize(publicationName) + "PublicationTo" + capitalize(repositoryName) + "Repository";

        TaskProvider<PublicationCheckerTask> publishingCheckTaskTaskProvider = tasks.register(taskName, PublicationCheckerTask.class, artifactPublication, publicationCheckerConfig);
        publishingCheckTaskTaskProvider.configure(task -> {
            task.setGroup("checks");
        });
    }

    private PublicationCheckerConfig getConfig() {
        PublicationCheckerExtension checkerExtension = project.getExtensions().findByType(PublicationCheckerExtension.class);
        return new PublicationCheckerConfig() {
            @Override
            public CheckResultStrategy getCheckResultStrategy() {
                CheckResultStrategy checkResultStrategy = checkerExtension.getCheckResultStrategy();
                if (checkResultStrategy == null) {
                    checkResultStrategy = CheckResultStrategies.FAIL_IF_EXISTENT;
                }
                return checkResultStrategy;
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
