package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.Artifact;

public class MavenArtifact implements Artifact {

    private String groupId;
    private String artifactId;
    private String version;
    private String classifier;
    private String extension;

    public MavenArtifact(String groupId, String artifactId, String version, String classifier, String extension) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.classifier = classifier;
        this.extension = extension;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getClassifier() {
        return classifier;
    }

    @Override
    public String getGroup() {
        return getGroupId();
    }

    @Override
    public String getName() {
        return getArtifactId();
    }

    public String getVersion() {
        return version;
    }

    public String getExtension() {
        return extension;
    }


    @Override
    public String toString() {
        return toCoordinates();
    }

    private String toCoordinates() {
        StringBuilder sb = new StringBuilder();

        sb.append(groupId).append(':').append(artifactId).append(':').append(version);
        if (classifier != null) {
            sb.append(':').append(classifier);
        }
        if (extension != null) {
            sb.append(':').append(extension);
        }

        return sb.toString();
    }
}
