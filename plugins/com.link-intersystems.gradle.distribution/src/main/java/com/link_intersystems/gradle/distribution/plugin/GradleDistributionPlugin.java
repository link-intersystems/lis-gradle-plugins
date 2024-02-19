package com.link_intersystems.gradle.distribution.plugin;

import com.link_intersystems.gradle.distribution.task.download.DownloadGradleDistTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.component.SoftwareComponentFactory;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Zip;
import org.gradle.language.base.plugins.LifecycleBasePlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public abstract class GradleDistributionPlugin implements Plugin<Project> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GradleDistributionPlugin.class);

    private final SoftwareComponentFactory componentFactory;

    @Inject
    public GradleDistributionPlugin(SoftwareComponentFactory componentFactory) {

        this.componentFactory = requireNonNull(componentFactory);
    }

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(BasePlugin.class);

        addExtension(project);

        TaskProvider<DownloadGradleDistTask> downloadTask = registerDownloadGradleDistTask(project);
        TaskProvider<Copy> unzipTask = registerUnzipTask(project, downloadTask);
        TaskProvider<Copy> copyInitDTask = copyInitDTask(project, unzipTask);
        TaskProvider<Zip> zipTask = registerZipTask(project, copyInitDTask);


        project.getTasks().named(LifecycleBasePlugin.BUILD_GROUP, task -> {
            task.dependsOn(zipTask);
        });
    }

    private TaskProvider<Copy> copyInitDTask(Project project, TaskProvider<Copy> unzipTask) {
        TaskProvider<Copy> copyInitDTask = project.getTasks().register("copyInitD", Copy.class, task -> {
            task.setGroup("gradle-dist");

            task.from(project.getLayout().getProjectDirectory().file("src/main/resources"));
            task.include("*");
            task.include("**/*");
            task.into(unzipTask.get().getDestinationDir());
        });


        copyInitDTask.get().dependsOn(unzipTask);

        return copyInitDTask;
    }

    private TaskProvider<Zip> registerZipTask(Project project, TaskProvider<Copy> preparedDirTask) {
        TaskProvider<Zip> zipTask = project.getTasks().register("zipDist", Zip.class, task -> {
            task.setGroup("gradle-dist");

            task.from(preparedDirTask.get().getDestinationDir());
            task.include("*");
            task.include("*/*");
            task.getArchiveFileName().set("custom-gradle-dist.zip");
        });

        zipTask.get().dependsOn(preparedDirTask);

        return zipTask;
    }

    private TaskProvider<Copy> registerUnzipTask(Project project, TaskProvider<DownloadGradleDistTask> downloadTask) {

        return project.getTasks().register("unzipDist", Copy.class, task -> {
            task.setGroup("gradle-dist");
            task.from(project.zipTree(downloadTask.get().getOutputFile()));
            task.into(project.getLayout().getBuildDirectory().file("gradle-dist/unzipped"));
        });

    }

    private TaskProvider<DownloadGradleDistTask> registerDownloadGradleDistTask(Project project) {
        TaskContainer tasks = project.getTasks();
        return tasks.register("download", DownloadGradleDistTask.class, task -> {
            task.setGroup("gradle-dist");
            task.getDownloadUrl().convention("https://services.gradle.org/distributions/gradle-8.6-bin.zip");

            GradleDistributionPluginExtension extension = project.getExtensions().getByType(GradleDistributionPluginExtension.class);

            String version = extension.getVersion();
            if (version != null) {
                task.getDownloadUrl().set("https://services.gradle.org/distributions/gradle-" + version + "-bin.zip");
            }

            Property<String> downloadUrl = task.getDownloadUrl();

            try {
                String downloadPath = new URI(downloadUrl.get()).getPath();
                String filename = downloadPath.substring(Math.max(0, downloadPath.lastIndexOf('/')));
                task.getOutputFile().convention(project.getLayout().getBuildDirectory().file("gradle-dist/download/" + filename));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    ;

    private void addExtension(Project project) {
        project.getExtensions().create(GradleDistributionPluginExtension.class, "gradleDist", DefaultGradleDistributionPluginExtension.class);
    }

    private URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}