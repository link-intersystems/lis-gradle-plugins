package com.link_intersystems.gradle.publication.plugins.verify;

import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.DefaultPolymorphicDomainObjectContainer;
import org.gradle.internal.reflect.Instantiator;

import javax.inject.Inject;

public class DefaultVerifyPublicationContainer extends DefaultPolymorphicDomainObjectContainer<VerifyPublication> implements VerifyPublicationContainer {

    @Inject
    public DefaultVerifyPublicationContainer(Instantiator instantiator, CollectionCallbackActionDecorator collectionCallbackActionDecorator) {
        super(VerifyPublication.class, instantiator, instantiator, collectionCallbackActionDecorator);
    }

}

