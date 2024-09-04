package com.link_intersystems.gradle.publication.plugins.verify.maven;

import com.link_intersystems.gradle.publication.ArtifactDesc;
import com.link_intersystems.gradle.publication.ArtifactPublication;
import com.link_intersystems.gradle.publication.VersionProvider;
import com.link_intersystems.gradle.publication.VersionProviders;
import com.link_intersystems.gradle.publication.maven.MavenArtifactCoordinates;
import com.link_intersystems.gradle.publication.maven.MavenArtifactCoordinatesFormat;
import com.link_intersystems.gradle.publication.maven.MavenArtifactPublication;
import com.link_intersystems.gradle.publication.maven.MavenArtifactRepositoryDesc;
import com.link_intersystems.gradle.publication.plugins.verify.AbstractVerifyPublication;
import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.UnknownDomainObjectException;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.logging.Logger;
import org.gradle.api.publish.Publication;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultVerifyMavenPublication extends AbstractVerifyPublication<MavenPublication, MavenArtifactCoordinates> implements VerifyMavenPublication {

    private Logger logger = (Logger) LoggerFactory.getLogger(DefaultVerifyMavenPublication.class);

    private MavenVerifyRepositoryHandler verifyRepositoriesHandler;

    public DefaultVerifyMavenPublication(String name, RepositoryHandler repositoryHandler) {
        super(name);
        verifyRepositoriesHandler = new MavenScopedVerifyRepositoryHandler(repositoryHandler);
    }

    public MavenVerifyRepositoryHandler getVerifyRepositories() {
        return verifyRepositoriesHandler;
    }

    @Override
    public List<ArtifactPublication> getArtifactPublications(Project project) {
        List<String> artifacts = getArtifacts();
        if (artifacts != null && !artifacts.isEmpty()) {
            logger.warn("Specified artifacts override current publication artifacts for verify publication task named '{}'", getName());
            List<MavenArtifactCoordinates> artifactCoords = artifacts.stream().map(this::parseCoords).collect(Collectors.toList());

            List<MavenArtifactRepository> artifactRepositories = getRepositories(project);

            return artifactRepositories.stream().map(repo -> {
                MavenArtifactRepositoryDesc repositoryDesc = MavenArtifactRepositoryDesc.of(repo);
                com.link_intersystems.gradle.publication.maven.MavenArtifactRepository repository = new com.link_intersystems.gradle.publication.maven.MavenArtifactRepository(repo);
                return new MavenArtifactPublication(repositoryDesc, repository, artifactCoords, getName());
            }).collect(Collectors.toList());
        }

        List<MavenArtifactRepository> artifactRepositories = getRepositories(project);
        return artifactRepositories.stream().map(repo -> MavenArtifactPublication.of((MavenPublicationInternal) getPublication(project), repo, getEffectiveVersionProvider())).collect(Collectors.toList());
    }


    private List<MavenArtifactRepository> getRepositories(Project project) {
        MavenVerifyRepositoryHandler verifyRepositoryHandler = getVerifyRepositories();

        List<MavenArtifactRepository> repositories = new ArrayList<>(verifyRepositoryHandler.getArtifactRepositories());

        if (repositories.isEmpty()) {
            repositories = project.getExtensions().getByType(PublishingExtension.class).getRepositories().withType(MavenArtifactRepository.class).stream().collect(Collectors.toList());
        }

        return repositories;
    }

    private Publication getPublication(Project project) {
        PublishingExtension publishingExtension = project.getExtensions().getByType(PublishingExtension.class);

        Publication publication = getPublication();
        if (publication == null) {
            logger.debug("No publication set for verify publication task '{}'. Resolve project publication named '{}' instead.", getName(), getName());
            try {

                publication = publishingExtension.getPublications().withType(MavenPublication.class).getByName(getName());
            } catch (UnknownDomainObjectException e) {
                List<String> availablePublications = publishingExtension.getPublications().withType(MavenPublication.class).stream().map(MavenPublication::getName).collect(Collectors.toList());
                String message = MessageFormat.format("Can not resolve a publication named ''{0}'' in project named ''{1}''." +
                        " Either specify one or rename the verify publication" +
                        " to match a publication of the project. Available publications {2}", getName(), project.getName(), availablePublications);
                throw new GradleException(message, e);
            }
        }

        return publication;
    }

    private MavenArtifactCoordinates parseCoords(String coords) {
        try {
            MavenArtifactCoordinates artifactCoordinates = new MavenArtifactCoordinatesFormat().parse(coords);

            String group = artifactCoordinates.getGroup();
            String name = artifactCoordinates.getName();
            String version = artifactCoordinates.getVersion();
            ArtifactDesc artifactDesc = new ArtifactDesc(group, name, version);

            VersionProvider effectiveVersionProvider = getEffectiveVersionProvider();
            String providedVersion = effectiveVersionProvider.getVersion(artifactDesc);

            return artifactCoordinates.withVersion(providedVersion);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse artifact coordinates '" + coords + "': " + e.getMessage(), e);
        }
    }

    public void verifyRepositories(Action<? super MavenVerifyRepositoryHandler> configure) {
        configure.execute(getVerifyRepositories());
    }

    public VersionProvider getEffectiveVersionProvider() {
        VersionProvider versionProvider = getVersionProvider();

        if (versionProvider == null) {
            versionProvider = VersionProviders.CURRENT_VERSION;
        }

        return versionProvider;
    }
}
