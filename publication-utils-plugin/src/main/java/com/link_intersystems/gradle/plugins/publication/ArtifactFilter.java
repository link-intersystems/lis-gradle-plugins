package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.ArtifactCoordinates;

public interface ArtifactFilter<T extends ArtifactCoordinates> {

    public static ArtifactFilter<ArtifactCoordinates> ALL = new ArtifactFilter<>() {
        @Override
        public boolean accept(ArtifactCoordinates coords) {
            return true;
        }

        @Override
        public String toString() {
            return "AllArtifactFilter";
        }
    };

    boolean accept(T coords);
}
