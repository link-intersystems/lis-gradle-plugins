package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.Artifact;
import com.link_intersystems.gradle.publication.ArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.transport.RepositoryTransport;
import org.gradle.internal.resource.ExternalResource;
import org.gradle.internal.resource.ExternalResourceName;
import org.gradle.internal.resource.ExternalResourceRepository;
import org.gradle.internal.resource.metadata.ExternalResourceMetaData;


public class MavenArtifactRepository implements ArtifactRepository {

    private final ExternalResourceRepository externalResourceRepository;
    private org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository;

    public MavenArtifactRepository(org.gradle.api.artifacts.repositories.MavenArtifactRepository mavenArtifactRepository) {
        this.mavenArtifactRepository = mavenArtifactRepository;
        String protocol = mavenArtifactRepository.getUrl().getScheme().toLowerCase();
        DefaultMavenArtifactRepository realRepository = (DefaultMavenArtifactRepository) mavenArtifactRepository;
        RepositoryTransport transport = realRepository.getTransport(protocol);
        externalResourceRepository = transport.getRepository();
    }

    @Override
    public boolean exists(Artifact artifact) {
        if (!(artifact instanceof MavenArtifact)) {
            throw new IllegalArgumentException("artifact must be a MavenArtifact.");
        }

        MavenArtifact mavenArtifact = (MavenArtifact) artifact;
        ExternalResourceName externalResourceName = toExternalResourceName(mavenArtifact);
        ExternalResource externalResource = externalResourceRepository.resource(externalResourceName);

        ExternalResourceMetaData metaData = externalResource.getMetaData();
        return metaData != null && !metaData.wasMissing();
    }

//    @Override
//    public Metadata getMetaData(MavenArtifact artifact) {
//        ExternalResourceName externalResourceName = getMetaDataResourceName(artifact);
//        return readMetaData(externalResourceName);
//    }


    public ExternalResourceName toExternalResourceName(MavenArtifact mavenArtifact) {
        String repositoryRelativePath = toRepositoryRelativePath(mavenArtifact);
        return new ExternalResourceName(mavenArtifactRepository.getUrl(), repositoryRelativePath);
    }

    private String toRepositoryRelativePath(MavenArtifact mavenArtifact) {
        String artifactId = mavenArtifact.getArtifactId();
        String artifactVersion = mavenArtifact.getVersion();

        StringBuilder path = new StringBuilder(toRepositoryRelativeBasePath(mavenArtifact));
        path.append(artifactId).append('-').append(artifactVersion);

        String classifier = mavenArtifact.getClassifier();
        if (classifier != null) {
            path.append('-').append(classifier);
        }

        String extension = mavenArtifact.getExtension();
        if (!extension.isEmpty()) {
            path.append('.').append(extension);
        }

        return path.toString();
    }

    private CharSequence toRepositoryRelativeBasePath(MavenArtifact mavenArtifact) {
        String groupId = mavenArtifact.getGroupId();
        String artifactId = mavenArtifact.getArtifactId();
        String moduleVersion = mavenArtifact.getVersion();

        StringBuilder path = new StringBuilder(128);
        String groupPath = groupId.replace('.', '/');
        path.append(groupPath).append('/');
        path.append(artifactId).append('/');
        path.append(moduleVersion).append('/');

        return path;
    }

    private ExternalResourceName getMetaDataResourceName(MavenArtifact mavenArtifact) {

        StringBuilder path = new StringBuilder(toRepositoryRelativeBasePath(mavenArtifact));
        path.append("maven-metadata.xml");

        return new ExternalResourceName(mavenArtifactRepository.getUrl(), path.toString());
    }

//    private Metadata readMetaData(ExternalResourceName metadataResource) {
//        ExternalResourceReadResult<Metadata> metadataExternalResourceReadResult = externalResourceRepository.resource(metadataResource).withContentIfPresent(inputStream -> {
//            try {
//                return new MetadataXpp3Reader().read(inputStream, false);
//            } catch (Exception e) {
//                throw UncheckedException.throwAsUncheckedException(e);
//            }
//        });
//
//        if (metadataExternalResourceReadResult == null) {
//            return null;
//        }
//
//        return metadataExternalResourceReadResult.getResult();
//    }

}
