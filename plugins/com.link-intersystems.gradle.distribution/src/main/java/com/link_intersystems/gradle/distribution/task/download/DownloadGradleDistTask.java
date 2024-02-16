package com.link_intersystems.gradle.distribution.task.download;

import com.link_intersystems.io.progress.ProgressInputStream;
import com.link_intersystems.net.http.HttpClient;
import com.link_intersystems.net.http.HttpHeader;
import com.link_intersystems.net.http.HttpHeaders;
import com.link_intersystems.net.http.HttpResponse;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.net.URL;
import java.util.List;

/**
 *
 */
public abstract class DownloadGradleDistTask extends DefaultTask {

    @Input
    public abstract Property<URL> getDownloadUrl();

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    /**
     *
     */
    @TaskAction
    public void download() {
        File outputFile = getOutputFile().get().getAsFile();
        ensureOutputFileDirsExist(outputFile);

        System.out.println("Downloading: " + getDownloadUrl().get());

        HttpClient httpClient = new HttpClient(new BugfixHttpRequestFactory());
        try {
            HttpResponse httpResponse = httpClient.get(getDownloadUrl().get().toString());
            try (InputStream in = getContentInputStream(httpResponse)) {
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                    copy(in, out);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureOutputFileDirsExist(File outputFile) {
        File dir = outputFile.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IllegalStateException("Unable to create output file directory " + dir);
            }
        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int read = 0;

        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
    }

    private InputStream getContentInputStream(HttpResponse httpResponse) throws IOException {
        InputStream content = httpResponse.getContent();

        return tryWrapProgressListener(httpResponse, content);
    }

    private InputStream tryWrapProgressListener(HttpResponse httpResponse, InputStream content) {
        HttpHeaders headers = httpResponse.getHeaders();
        HttpHeader contentLengthHeader = headers.get("Content-Length");
        if (contentLengthHeader != null) {
            List<String> values = contentLengthHeader.getValues();
            String contentLengthValue = values.get(0);
            int contentLength = Integer.parseInt(contentLengthValue);
            if (contentLength > 0) {
                content = new ProgressInputStream(content, contentLength, new DownloadProgressListener());
            }
        }
        return content;
    }

}
