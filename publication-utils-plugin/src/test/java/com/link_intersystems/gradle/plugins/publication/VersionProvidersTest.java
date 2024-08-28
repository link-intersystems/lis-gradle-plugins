package com.link_intersystems.gradle.plugins.publication;

import com.link_intersystems.gradle.publication.ArtifactDesc;
import org.junit.jupiter.api.Test;

import static com.link_intersystems.gradle.publication.VersionProviders.CURRENT_VERSION;
import static com.link_intersystems.gradle.publication.VersionProviders.RELEASE_VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VersionProvidersTest {

    @Test
    void releaseVersion() {
        assertEquals("1.2.3", RELEASE_VERSION.getVersion(new ArtifactDesc("", "", "1.2.3-SNAPSHOT")));
        assertEquals("1.2.3", RELEASE_VERSION.getVersion(new ArtifactDesc("", "", "1.2.3")));
    }


    @Test
    void currentVersion() {
        assertEquals("1.2.3-SNAPSHOT", CURRENT_VERSION.getVersion(new ArtifactDesc("", "", "1.2.3-SNAPSHOT")));
        assertEquals("1.2.3", CURRENT_VERSION.getVersion(new ArtifactDesc("", "", "1.2.3")));
    }
}