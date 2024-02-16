package com.link_intersystems.gradle.distribution.task.download;

import com.link_intersystems.io.progress.ProgressEvent;
import com.link_intersystems.io.progress.ProgressListener;
import com.link_intersystems.math.LinearEquation;
import com.link_intersystems.math.TwoPointLinearEquation;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.DOWN;

public class DownloadProgressListener implements ProgressListener {

    public static final BigDecimal ONE_HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);
    private BigDecimal latestPercentValue = BigDecimal.ONE.negate();

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        int min = progressEvent.getMin();
        int max = progressEvent.getMax();
        int count = max - min;

        int value = progressEvent.getValue();

        LinearEquation percentEquation = new TwoPointLinearEquation(count, 100);
        double percentValue = percentEquation.fX(value);
        BigDecimal percent = BigDecimal.valueOf(percentValue).setScale(0, DOWN);

        if (reportProgress(percent)) {
            if (percent.compareTo(latestPercentValue) > 0) {
                latestPercentValue = percent;
                if (percent.compareTo(ZERO) > 0) {
                    System.out.print("...");
                }
                System.out.print(percent + "%");
            }
        }

        if (ONE_HUNDRED.equals(percent)) {
            System.out.println();
        }
    }

    private boolean reportProgress(BigDecimal percent) {
        return ZERO.compareTo(percent.remainder(BigDecimal.valueOf(10))) == 0;
    }
}
