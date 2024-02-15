package com.link_intersystems.gradle.distribution.plugin;

import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.tasks.SourceSetContainer;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public class DefaultGradleDistributionPluginExtension implements GradleDistributionPluginExtension {

    private final ProjectInternal project;
    private final SourceSetContainer sourceSets;

    private String gradleVersion;

    @Inject
    public DefaultGradleDistributionPluginExtension(ProjectInternal project, SourceSetContainer sourceSets) {

        this.project = requireNonNull(project);
        this.sourceSets = requireNonNull(sourceSets);
    }

    @Override
    public String getVersion() {
        return gradleVersion;
    }

    @Override
    public void setVersion(String version) {
        this.gradleVersion = version;
    }
}
