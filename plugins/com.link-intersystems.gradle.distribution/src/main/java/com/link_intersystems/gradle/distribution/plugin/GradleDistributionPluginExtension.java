package com.link_intersystems.gradle.distribution.plugin;

import org.gradle.api.file.RegularFile;

/**
 *
 */
public interface GradleDistributionPluginExtension {

    public String getVersion();

    public void setVersion(String version);

}
