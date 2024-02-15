package com.link_intersystems.gradle.distribution.plugin;

import com.link_intersystems.gradle.distribution.task.download.DownloadGradleDistArgs;
import org.gradle.api.Project;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import static java.util.Objects.requireNonNull;

public class ExtensionDownloadGradleDistArgs implements DownloadGradleDistArgs {

    private static final String DEFAULT_GRADLE_VERSION = "8.6";
    private final Project project;
    private GradleDistributionPluginExtension extension;


    public ExtensionDownloadGradleDistArgs(Project project, GradleDistributionPluginExtension extension) {
        this.project = requireNonNull(project);
        this.extension = requireNonNull(extension);
    }

    @Override
    public URL getDownloadUrl() {
        String downloadUrlFormat = "https://services.gradle.org/distributions/gradle-{0}-bin.zip";

        String downloadUrlString = MessageFormat.format(downloadUrlFormat, getDistVersion());

        try {
            return new URL(downloadUrlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDistVersion() {
        String gradleVersion = extension.getVersion();

        if (gradleVersion == null) {
            return DEFAULT_GRADLE_VERSION;
        }

        return gradleVersion;
    }

    @Override
    public File getOutputFile() {
        URL downloadUrl = getDownloadUrl();
        String path = downloadUrl.getPath();
        int lastIndexOf = path.lastIndexOf('.');
        String extension = "";
        if (lastIndexOf > -1) {
            extension = path.substring(lastIndexOf);
        }
        return project.getLayout().getBuildDirectory().file("gradle-dist/" + getDistVersion() + "/gradle-" + getDistVersion() + extension).get().getAsFile();
    }
}
