package com.link_intersystems.gradle.distribution.task.download;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public class DownloadGradleDistTask extends DefaultTask {


    private final DownloadGradleDistArgs args;

    /**
     * @param args
     */
    @Inject
    public DownloadGradleDistTask(DownloadGradleDistArgs args) {

        this.args = requireNonNull(args);
    }

    /**
     *
     */
    @TaskAction
    public void download() {
        URL downloadUrl = args.getDownloadUrl();
        File outputFile = args.getOutputFile();

        File dir = outputFile.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException("Unable to create output file directory " + dir);
            }
        }

        try {
            URLConnection urlConnection = downloadUrl.openConnection();
            try (InputStream in = urlConnection.getInputStream()) {
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {

                    byte[] buffer = new byte[8192];
                    int read = 0;

                    while ((read = in.read(buffer)) > 0) {
                        out.write(buffer, 0, read);
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
