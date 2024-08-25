package com.link_intersystems.gradle.publication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArtifactCoordinatesPublicationProviderTest {

    @Test
    void getProviders() {
        ArtifactPublicationProviders providers = ArtifactPublicationProvider.getProviders();
        assertEquals(1, providers.size());

    }
}