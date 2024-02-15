package com.link_intersystems.gradle.distribution.task.download;

import java.io.File;
import java.net.URL;

public interface DownloadGradleDistArgs {
    URL getDownloadUrl();

    String getDistVersion();

    File getOutputFile();
}
