package com.link_intersystems.gradle.distribution.plugin;

import com.link_intersystems.gradle.distribution.task.download.DownloadGradleDistArgs;
import com.link_intersystems.gradle.distribution.task.download.DownloadGradleDistTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;

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
        tasks.register(taskName, DownloadGradleDistTask.class, downloadGradleDistArgs);
        tasks.withType(DownloadGradleDistTask.class, task -> {
            task.setGroup(GROUP_NAME);
        });
    }
}
