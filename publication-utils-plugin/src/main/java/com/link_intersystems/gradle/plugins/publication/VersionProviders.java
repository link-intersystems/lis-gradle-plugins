package com.link_intersystems.gradle.plugins.publication;

/**
 * Common {@link VersionProvider}s.
 */
public class VersionProviders {

    /**
     * Returns the same version as it gets passed.
     */
    public static final VersionProvider CURRENT_VERSION = (group, name, version) -> version;

    /**
     * Provides the release version by cutting of the -SNAPSHOT from the version if any is present.
     */
    public static final VersionProvider RELEASE_VERSION = new VersionProvider() {

        public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

        @Override
        public String getVersion(String group, String name, String version) {
            if (version.endsWith(SNAPSHOT_SUFFIX)) {
                version = version.substring(0, version.length() - SNAPSHOT_SUFFIX.length());
            }

            return version;
        }
    };
}
