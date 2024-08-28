package com.link_intersystems.gradle.publication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArtifactPublicationProvidersTest {

    @Test
    void getProviders() {
        ArtifactPublicationProviders providers = ArtifactPublicationProviders.get();
        assertEquals(1, providers.size());

    }
}