package com.link_intersystems.gradle.publication.plugins;

import org.gradle.api.plugins.ExtensionAware;

/**
 * An {@link ExtensionAware} configuration for the {@link PublicationUtilsPlugin}, that allows
 * multiple utils to add extensions.
 *
 * @see PublicationUtil
 */
public interface PublicationUtilsExtension extends ExtensionAware {
    String NAME = "publications";

}
