package com.link_intersystems.gradle.plugins.publishing.check;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.transport.RepositoryTransport;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.maven.internal.publisher.MavenNormalizedPublication;
import org.gradle.api.publish.maven.internal.publisher.MavenPublishers;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.resource.ExternalResource;
import org.gradle.internal.resource.ExternalResourceName;
import org.gradle.internal.resource.ExternalResourceRepository;
import org.gradle.internal.resource.metadata.ExternalResourceMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URI;

public class PublishingCheckTask extends DefaultTask {

    private URI repositoryUrl;
    private Logger logger = LoggerFactory.getLogger(PublishingCheckTask.class);

    private final MavenNormalizedPublication publication;
    private final MavenArtifactRepository mavenArtifactRepository;

    @Inject
    public PublishingCheckTask(MavenPublicationInternal publication, MavenArtifactRepository mavenArtifactRepository) {
        this.publication = publication.asNormalisedPublication();
        this.mavenArtifactRepository = mavenArtifactRepository;
        repositoryUrl = mavenArtifactRepository.getUrl();
    }

    @Inject
    protected MavenPublishers getMavenPublishers() {
        throw new UnsupportedOperationException();
    }

    @TaskAction
    public void checkPublishing() {
        String protocol = repositoryUrl.getScheme().toLowerCase();
        DefaultMavenArtifactRepository realRepository = (DefaultMavenArtifactRepository) mavenArtifactRepository;
        RepositoryTransport transport = realRepository.getTransport(protocol);
        ExternalResourceRepository repository = transport.getRepository();


        if (publication.getMainArtifact() != null) {
            ExternalResourceName externalResourceName = getExternalResourceName(null, publication.getMainArtifact().getExtension());
            ExternalResource externalResource = repository.resource(externalResourceName);
            ExternalResourceMetaData metaData = externalResource.getMetaData();
            logger.info(">>>>>>>>>>>>>>>> " + metaData.wasMissing());
        }
    }

    private ExternalResourceName getExternalResourceName(String classifier, String extension) {
        String groupId = publication.getGroupId();
        String artifactId = publication.getArtifactId();
        String moduleVersion = publication.getVersion();
        String artifactVersion = publication.getVersion();

        StringBuilder path = new StringBuilder(128);
        String groupPath = groupId.replace('.', '/');
        path.append(groupPath).append('/');
        path.append(artifactId).append('/');
        path.append(moduleVersion).append('/');
        path.append(artifactId).append('-').append(artifactVersion);

        if (classifier != null) {
            path.append('-').append(classifier);
        }
        if (extension.length() > 0) {
            path.append('.').append(extension);
        }


        return new ExternalResourceName(repositoryUrl, path.toString());
    }
}
