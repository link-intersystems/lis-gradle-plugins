package com.link_intersystems.gradle.distribution.plugin;

import org.gradle.api.internal.project.ProjectInternal;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public class DefaultGradleDistributionPluginExtension implements GradleDistributionPluginExtension {

    private String gradleVersion;

    @Override
    public String getVersion() {
        return gradleVersion;
    }

    @Override
    public void setVersion(String version) {
        this.gradleVersion = version;
    }
}
