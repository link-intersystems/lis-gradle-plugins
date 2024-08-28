package com.link_intersystems.gradle.publication;

public interface VersionProvider {

    String getVersion(ArtifactDesc artifact);
}
