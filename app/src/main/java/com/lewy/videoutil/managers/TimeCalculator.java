package com.lewy.videoutil.managers;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by lewy on 2016-06-03.
 */
public class TimeCalculator {

    private static final int MINUTE = 60;

    private static NumberFormat formatter = new DecimalFormat("00");

    public static String secondsToString(int seconds) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(seconds / MINUTE)
                .append(":")
                .append(formatter.format(seconds % MINUTE));

        return stringBuilder.toString();
    }
}
