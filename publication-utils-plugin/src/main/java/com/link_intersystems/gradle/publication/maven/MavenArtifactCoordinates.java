package com.link_intersystems.gradle.publication.maven;

import com.link_intersystems.gradle.publication.ArtifactCoordinates;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class MavenArtifactCoordinates implements ArtifactCoordinates {

    private String groupId;
    private String artifactId;
    private String version;
    private String extension;
    private String classifier;

    public MavenArtifactCoordinates(String groupId, String artifactId, String version, String extension) {
        this(groupId, artifactId, version, extension, null);
    }

    public MavenArtifactCoordinates(String groupId, String artifactId, String version, String extension, String classifier) {
        this.groupId = requireNonBlank(groupId, "groupId must not be blank.");
        this.artifactId = requireNonBlank(artifactId, "artifactId must not be blank.");
        this.version = requireNonBlank(version, "version must not be blank.");
        this.extension = requireNonBlank(extension, "extension must not be blank.");
        this.classifier = classifier == null ? null : requireNonBlank(classifier, "classifier must not be blank if provided.");
    }

    private static String requireNonBlank(String value, String message) {
        String nonNullValue = requireNonNull(value, message);
        if (nonNullValue.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MavenArtifactCoordinates that = (MavenArtifactCoordinates) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId) &&
                Objects.equals(version, that.version) &&
                Objects.equals(extension, that.extension) &&
                Objects.equals(classifier, that.classifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version, extension, classifier);
    }

    @Override
    public String toString() {
        return MavenArtifactCoordinatesFormat.formatMavenArtifact(this);
    }

}
