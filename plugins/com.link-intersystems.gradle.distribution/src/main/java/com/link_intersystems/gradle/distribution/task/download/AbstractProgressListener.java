package com.link_intersystems.gradle.distribution.task.download;

import com.link_intersystems.io.progress.ProgressEvent;
import com.link_intersystems.io.progress.ProgressListener;
import com.link_intersystems.math.LinearEquation;
import com.link_intersystems.math.TwoPointLinearEquation;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.DOWN;

public abstract class AbstractProgressListener implements ProgressListener {

    private static final BigDecimal ONE_HUNDRED = TEN.multiply(TEN);
    public static final BigDecimal MINUS_ONE = BigDecimal.ONE.negate();
    private BigDecimal latestPercentValue = MINUS_ONE;
    private BigDecimal latestReportedValue = MINUS_ONE;


    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        if (MINUS_ONE.compareTo(latestPercentValue) == 0) {
            onBegin();
        }

        int min = progressEvent.getMin();
        int max = progressEvent.getMax();
        int totalWork = max - min;

        int value = progressEvent.getValue();

        LinearEquation percentEquation = new TwoPointLinearEquation(totalWork, 100);
        double percentValue = percentEquation.fX(value);
        BigDecimal percent = BigDecimal.valueOf(percentValue).setScale(0, DOWN);
        latestPercentValue = percent;

        if (shouldReport(percent)) {
            if (percent.compareTo(latestReportedValue) != 0) {
                latestReportedValue = percent;
                doReportProgress(percent);
            }
        }

        if (ONE_HUNDRED.compareTo(percent) == 0) {
            onEnd();
        }
    }

    protected void onEnd() {
    }

    protected void onBegin() {
    }

    protected boolean shouldReport(BigDecimal percent) {
        return ZERO.compareTo(percent.remainder(BigDecimal.valueOf(10))) == 0;
    }

    protected abstract void doReportProgress(BigDecimal percent);
}
