package com.link_intersystems.gradle.plugins.publication.utils;

import com.link_intersystems.gradle.plugins.publication.verify.VerifyPublicationContainer;
import org.gradle.api.Action;

import javax.inject.Inject;

public class DefaultPublicationUtilsExtension implements PublicationUtilsExtension {

    private VerifyPublicationContainer verifyPublicationContainer;

    @Inject
    public DefaultPublicationUtilsExtension(VerifyPublicationContainer verifyPublicationContainer) {
        this.verifyPublicationContainer = verifyPublicationContainer;
    }

    @Override
    public VerifyPublicationContainer getVerify() {
        return verifyPublicationContainer;
    }

    @Override
    public void verify(Action<VerifyPublicationContainer> verifyPublicationExtensionAction) {
        verifyPublicationExtensionAction.execute(verifyPublicationContainer);
    }
}

