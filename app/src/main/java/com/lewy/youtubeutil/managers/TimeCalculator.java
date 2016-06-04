package com.lewy.youtubeutil.managers;

/**
 * Created by dawid on 2016-06-03.
 */
public class TimeCalculator {

    private static final int MINUTE = 60;

    public static String secondsToString(int seconds) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(seconds / MINUTE)
                .append(":")
                .append(seconds % MINUTE);

        return stringBuilder.toString();
    }
}
