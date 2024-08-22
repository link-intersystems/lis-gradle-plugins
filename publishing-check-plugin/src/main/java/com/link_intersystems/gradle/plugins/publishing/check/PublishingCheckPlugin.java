package com.link_intersystems.gradle.plugins.publishing.check;

import org.gradle.api.NamedDomainObjectList;
import org.gradle.api.NamedDomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.plugins.PublishingPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishingCheckPlugin implements Plugin<Project> {

    public static final String CHECK_PUBPLISHING = "checkPublishing";

    private Logger logger = LoggerFactory.getLogger(PublishingCheckPlugin.class);

    @Override
    public void apply(Project p) {
        final TaskContainer tasks = p.getTasks();

        p.getExtensions().configure(PublishingExtension.class, extension -> {
            realizeCheckPublishingTasksLater(p, extension);
        });
    }

    private void realizeCheckPublishingTasksLater(final Project project, final PublishingExtension extension) {
        final NamedDomainObjectSet<MavenPublicationInternal> mavenPublications = extension.getPublications().withType(MavenPublicationInternal.class);
        final TaskContainer tasks = project.getTasks();

        final NamedDomainObjectList<MavenArtifactRepository> repositories = extension.getRepositories().withType(MavenArtifactRepository.class);

        mavenPublications.all(publication -> {
            createCheckPublishTasksForEachMavenRepo(tasks, publication, repositories);
        });
    }

    private void createCheckPublishTasksForEachMavenRepo(final TaskContainer tasks, final MavenPublicationInternal publication, final NamedDomainObjectList<MavenArtifactRepository> repositories) {
        final String publicationName = publication.getName();
        repositories.all(repository -> {
            final String repositoryName = repository.getName();
            final String taskName = "checkPublish" + capitalize(publicationName) + "PublicationTo" + capitalize(repositoryName) + "Repository";
            TaskProvider<PublishingCheckTask> publishingCheckTaskTaskProvider = tasks.register(taskName, PublishingCheckTask.class, publication, repository);
            publishingCheckTaskTaskProvider.configure(task -> {
                task.setGroup(PublishingPlugin.PUBLISH_TASK_GROUP);
            });
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
