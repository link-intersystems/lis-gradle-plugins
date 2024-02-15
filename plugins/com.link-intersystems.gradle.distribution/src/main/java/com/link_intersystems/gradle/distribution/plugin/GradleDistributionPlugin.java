package com.link_intersystems.gradle.distribution.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JvmEcosystemPlugin;
import org.gradle.api.tasks.SourceSetContainer;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public abstract class GradleDistributionPlugin implements Plugin<Project> {

    private final ObjectFactory objectFactory;

    /**
     * @param objectFactory the gradle {@link ObjectFactory}.
     */
    @Inject
    public GradleDistributionPlugin(ObjectFactory objectFactory) {

        this.objectFactory = requireNonNull(objectFactory);
    }

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(BasePlugin.class);
        project.getPluginManager().apply(JvmEcosystemPlugin.class);


        DefaultGradleDistributionPluginExtension extension = addExtension(project);
        GradleDistributionTaskGroup taskGroup = new GradleDistributionTaskGroup(extension);
        project.afterEvaluate(taskGroup::configureTasks);
    }

    private DefaultGradleDistributionPluginExtension addExtension(Project project) {
        SourceSetContainer sourceSets = (SourceSetContainer) project.getExtensions().getByName("sourceSets");

        return (DefaultGradleDistributionPluginExtension) project.getExtensions().create(GradleDistributionPluginExtension.class, "gradleDist", DefaultGradleDistributionPluginExtension.class, project, sourceSets);
    }

}