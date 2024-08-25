package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.VerifyPublicationResult;

public interface VerifyMode {
    void handle(VerifyPublicationResult verifyPublicationResult);
}
