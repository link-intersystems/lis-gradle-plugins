package com.link_intersystems.gradle.distribution.plugin;

import org.gradle.api.file.RegularFile;

/**
 *
 */
public class DefaultGradleDistributionPluginExtension implements GradleDistributionPluginExtension {

    private String gradleVersion;
    private RegularFile outputFile;

    @Override
    public String getVersion() {
        return gradleVersion;
    }

    @Override
    public void setVersion(String version) {
        this.gradleVersion = version;
    }

}
