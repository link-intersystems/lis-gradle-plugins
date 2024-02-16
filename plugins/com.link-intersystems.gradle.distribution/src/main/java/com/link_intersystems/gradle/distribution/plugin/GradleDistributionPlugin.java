package com.link_intersystems.gradle.distribution.plugin;

import com.link_intersystems.gradle.distribution.task.download.DownloadGradleDistTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JvmEcosystemPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;

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

        TaskContainer tasks = project.getTasks();
        TaskProvider<DownloadGradleDistTask> taskProvider = tasks.register("download", DownloadGradleDistTask.class, task -> {
            task.setGroup("gradle-dist");
            task.getDownloadUrl().convention(toURL("https://services.gradle.org/distributions/gradle-8.6-bin.zip"));

            String version = extension.getVersion();
            if (version != null) {
                task.getDownloadUrl().set(toURL("https://services.gradle.org/distributions/gradle-" + version + "-bin.zip"));
            }

            task.getOutputFile().convention(project.getLayout().getBuildDirectory().file("gradle-dist/download.zip"));
        });



        project.getTasks().named(LifecycleBasePlugin.BUILD_GROUP, task -> {
            task.dependsOn(taskProvider);
        });
    }

    private DefaultGradleDistributionPluginExtension addExtension(Project project) {
        return (DefaultGradleDistributionPluginExtension) project.getExtensions().create(GradleDistributionPluginExtension.class, "gradleDist", DefaultGradleDistributionPluginExtension.class);
    }

    private URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}