package com.link_intersystems.gradle.distribution.task.download;

import org.gradle.api.logging.Logger;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.util.Objects.requireNonNull;

public class DownloadProgressListener extends AbstractProgressListener {

    private final Logger logger;

    public DownloadProgressListener(Logger logger) {
        this.logger = requireNonNull(logger);
    }


    @Override
    protected void doReportProgress(BigDecimal percent) {

        logger.lifecycle(percent + "%");
    }

}
