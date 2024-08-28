package com.link_intersystems.gradle.publication;

/**
 * Common {@link VersionProvider}s.
 */
public class VersionProviders {

    /**
     * Returns the same version as it gets passed.
     */
    public static final VersionProvider CURRENT_VERSION = ArtifactDesc::getVersion;

    /**
     * Provides the release version by cutting of the -SNAPSHOT from the version if any is present.
     */
    public static final VersionProvider RELEASE_VERSION = new VersionProvider() {

        public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

        @Override
        public String getVersion(ArtifactDesc artifact) {
            if (artifact.getVersion().endsWith(SNAPSHOT_SUFFIX)) {
                artifact.setVersion(artifact.getVersion().substring(0, artifact.getVersion().length() - SNAPSHOT_SUFFIX.length()));
            }

            return artifact.getVersion();
        }
    };
}
