package com.jelvix.kotlincodesample.util;

import android.content.res.Resources;

import com.jelvix.kotlincodesample.KotlinDemoApplication;
import com.jelvix.kotlincodesample.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Jelvix demo CodeSample
 * Copyright © 2017 Jelvix. All rights reserved.
 */

public class DateTimeHelper {

    public static final String PATTERN_DATE_WITH_YEAR = "MMM d, YYYY";
    public static final String PATTERN_DATE_SHORT = "MMM d";

    private static final int MILLISECONDS_IN_SECOND = 1000;
    private static final int MILLISECONDS_IN_MINUTE = MILLISECONDS_IN_SECOND * 60;
    private static final int MINUTE_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_WEEK = 7;

    public static String convertTime(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        long timeInMillis = date.getTime();
        long diff = currentCalendar.getTimeInMillis() - timeInMillis;

        int currentYear = currentCalendar.get(Calendar.YEAR);
        int year = dateCalendar.get(Calendar.YEAR);

        final long minutes = diff / MILLISECONDS_IN_MINUTE;
        final long seconds = diff / MILLISECONDS_IN_SECOND;
        final long hours = minutes / MINUTE_IN_HOUR;
        final long days = hours / HOURS_IN_DAY;
        final long weeks = days / DAYS_IN_WEEK;

        String time;
        Resources resources = KotlinDemoApplication.getAppComponent().provideResources();
        if (weeks > 0) {
            if (currentYear == year) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimeHelper.PATTERN_DATE_SHORT, Locale.US);
                time = simpleDateFormat.format(timeInMillis);
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimeHelper.PATTERN_DATE_WITH_YEAR, Locale.US);
                time = simpleDateFormat.format(timeInMillis);
            }
        } else if (days > 0) {
            time = resources.getString(R.string.day, days);
        } else if (hours > 0) {
            time = resources.getString(R.string.hour, hours);
        } else if (minutes > 0) {
            time = resources.getString(R.string.minute, minutes);
        } else {
            time = resources.getString(R.string.second, seconds);
        }
        return time;
    }
}
