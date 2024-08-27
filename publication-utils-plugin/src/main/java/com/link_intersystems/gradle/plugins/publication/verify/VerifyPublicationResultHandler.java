package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.VerifyPublicationResult;

public interface VerifyPublicationResultHandler {
    void handle(VerifyPublicationResult verifyPublicationResult);
}
