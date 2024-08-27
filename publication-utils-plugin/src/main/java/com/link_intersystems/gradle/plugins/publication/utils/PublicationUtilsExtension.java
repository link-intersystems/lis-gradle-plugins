package com.link_intersystems.gradle.plugins.publication.utils;

import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationContainer;
import org.gradle.api.Action;

public interface PublicationUtilsExtension {
    String NAME = "publications";

    VerifyPublicationContainer getVerify();

    void verify(Action<VerifyPublicationContainer> verifyPublicationExtensionAction);
}
