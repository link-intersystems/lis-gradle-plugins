package com.link_intersystems.gradle.plugins.publication.utils;

import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationExtension;
import org.gradle.api.Action;

public class PublicationUtilsExtension {

    public static final String NAME = "publications";

    private VerifyPublicationExtension verifyPublicationExtension = new VerifyPublicationExtension();

    public VerifyPublicationExtension getVerify() {
        return verifyPublicationExtension;
    }

    public void verify(Action<VerifyPublicationExtension> verifyPublicationExtensionAction) {
        verifyPublicationExtensionAction.execute(verifyPublicationExtension);
    }
}

