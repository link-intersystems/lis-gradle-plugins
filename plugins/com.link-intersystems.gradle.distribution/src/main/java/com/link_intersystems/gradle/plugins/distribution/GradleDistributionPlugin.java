package com.link_intersystems.gradle.plugins.distribution;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.BasePlugin;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public abstract class GradleDistributionPlugin implements Plugin<Project> {

    private final ObjectFactory objectFactory;

    /**
     *
     * @param objectFactory the gradle {@link ObjectFactory}.
     */
    @Inject
    public GradleDistributionPlugin(ObjectFactory objectFactory) {

        this.objectFactory = requireNonNull(objectFactory);
    }

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(BasePlugin.class);


    }

}