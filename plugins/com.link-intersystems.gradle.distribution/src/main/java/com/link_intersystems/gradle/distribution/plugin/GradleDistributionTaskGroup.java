package com.link_intersystems.gradle.distribution.plugin;

import com.link_intersystems.gradle.distribution.task.download.DownloadGradleDistArgs;
import com.link_intersystems.gradle.distribution.task.download.DownloadGradleDistTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import static java.util.Objects.requireNonNull;

public class GradleDistributionTaskGroup {

    public static final String GROUP_NAME = "gradle-dist";
    private GradleDistributionPluginExtension extension;

    public GradleDistributionTaskGroup(GradleDistributionPluginExtension extension) {
        this.extension = requireNonNull(extension);
    }

    public void configureTasks(Project project) {
        TaskContainer tasks = project.getTasks();
        DownloadGradleDistArgs downloadGradleDistArgs = new ExtensionDownloadGradleDistArgs(project, extension);
        String taskName = MessageFormat.format("downloadGradleDist-{0}", downloadGradleDistArgs.getDistVersion());
        TaskProvider<DownloadGradleDistTask> taskProvider = tasks.register(taskName, DownloadGradleDistTask.class);
        taskProvider.configure(task -> {
            task.setGroup(GROUP_NAME);
            task.getOutputFile().convention(project.getLayout().getBuildDirectory().file("gradle-dist.zip"));
            try {
                task.getDownloadUrl().convention(new URL("https://services.gradle.org/distributions/gradle-8.6-bin.zip"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
