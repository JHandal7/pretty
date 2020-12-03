package com.suusoft.restaurantuser.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Suusoft.
 * Created 06/13/16. v1.0
 */

public class DateTimeUtility {

    private static final String DEFAULT_FORMAT_DATETIME_SERVER = "yyyy-MM-dd";
    private static final String DEFAULT_FORMAT_FULL_DATETIME_SERVER = "yyyy-MM-dd hh:mm:ss";

    private static final String DEFAULT_FORMAT_DATE = "dd MMM yyyy";
    private static final String DEFAULT_FORMAT_DATETIME = "MMM dd, yyyy hh:mm aa";
    private static final String DEFAULT_FORMAT_TIME = "hh:mm aa";
    private static final String DEFAULT_FORMAT_DATE_APP = "dd MMM yyyy";

    public static String convertTimeStampToDate(String timeStamp) {
        SimpleDateFormat formater = new SimpleDateFormat(DEFAULT_FORMAT_DATETIME,
                Locale.getDefault());
        try {
            Date date = new Date(Long.parseLong(timeStamp) * 1000);
            return formater.format(date);
        } catch (NumberFormatException ex) {
            return formater.format(new Date());
        }
    }

    public static String convertTimeStampToDate(long timeStamp) {
        SimpleDateFormat formater = new SimpleDateFormat(DEFAULT_FORMAT_DATE_APP,
                Locale.getDefault());
        try {
            Date date = new Date(timeStamp * 1000);
            return formater.format(date);
        } catch (NumberFormatException ex) {
            return formater.format(new Date());
        }
    }

    public static String convertTimeStampToDate(long timeStamp, String output) {
        SimpleDateFormat formater = new SimpleDateFormat(output,
                Locale.getDefault());
        try {
            Date date = new Date(timeStamp * 1000);
            return formater.format(date);
        } catch (NumberFormatException ex) {
            return formater.format(new Date());
        }
    }

    public static String convertTimeStampToTime(String timeStamp) {
        SimpleDateFormat formater = new SimpleDateFormat(DEFAULT_FORMAT_TIME,
                Locale.getDefault());
        try {
            Date date = new Date(Long.parseLong(timeStamp) * 1000);
            return formater.format(date);
        } catch (NumberFormatException ex) {
            return formater.format(new Date());
        }
    }

    public static String convertDatetoTimeStamp(Date date) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        try {
            result = cal.getTimeInMillis() + "";
        } catch (Exception ex) {
            result = "";
        }
        return result;
    }

    /**
     * Convert a date string to a date string other format
     * exam: input is a string 2016-06-16 15:00
     * output is a string: June, 16th 2016 15:00
     */
    public static String convertStringToDate(String strDate) {
        if (strDate != null && !strDate.equals("null") && !strDate.isEmpty() && !strDate.equals("0")) {
            SimpleDateFormat from = new SimpleDateFormat(DEFAULT_FORMAT_FULL_DATETIME_SERVER);
            SimpleDateFormat to = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
            Date date = null;
            try {
                date = from.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Log.e("TAG DATE", "convertStringToDate: " + strDate );
            return to.format(date);
        } else {
            SimpleDateFormat to = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
            return to.format(new Date());
        }


    }

    public static String convertStringToDate(String strDate, String input, String output) {
        if (strDate != null && !strDate.equals("null") && !strDate.isEmpty() && !strDate.equals("0")) {
            SimpleDateFormat from = new SimpleDateFormat(input);
            SimpleDateFormat to = new SimpleDateFormat(output);
            Date date = null;
            try {
                date = from.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return to.format(date);
        } else {
            SimpleDateFormat to = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
            return to.format(new Date());
        }
    }
    public static String convertStringToDate(String strDate, String output) {
        if (strDate != null && !strDate.equals("null") && !strDate.isEmpty() && !strDate.equals("0")) {
            SimpleDateFormat from = new SimpleDateFormat(DEFAULT_FORMAT_FULL_DATETIME_SERVER);
            SimpleDateFormat to = new SimpleDateFormat(output);
            Date date = null;
            try {
                date = from.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return to.format(date);
        } else {
            SimpleDateFormat to = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
            return to.format(new Date());
        }
    }

    /**
     * Get today in format
     *
     * @return a date format string
     */
    public static String getToday() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
        return df.format(c.getTime());
    }

    /**
     * Get yesterday in format
     *
     * @return a date format string
     */
    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
        return df.format(cal.getTime());
    }

    /**
     * Calculate Date diff two days
     */
    public static long getDateDiff(Date curDate, Date specDate, TimeUnit timeUnit) {
        long diff = curDate.getTime() - specDate.getTime();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int getDateDiff(long curTimeStamp, long specTimeStamp) {
        try {
            long diff = specTimeStamp - curTimeStamp;
            long day = diff / 24 / 60 / 60;

            String strDay = day + "";
            if (strDay.contains(".")) {
                strDay = strDay.substring(0, strDay.indexOf("."));
            }

            return Integer.parseInt(strDay);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static ArrayList<String> countDown(long curTimeStamp, long specTimeStamp) {
        ArrayList<String> arr = new ArrayList<>();

        long diff = specTimeStamp - curTimeStamp;

        long sec = diff % 60;
        long min = (diff / 60) % 60;
        long hour = (diff / 60 / 60) % 24;
        long day = diff / 24 / 60 / 60;

        String strSec = sec + "";
        if (sec < 10) {
            strSec = "0" + sec;
        }
        if (strSec.contains(".")) {
            strSec = strSec.substring(0, strSec.indexOf("."));
        }

        String strMin = min + "";
        if (min < 10) {
            strMin = "0" + min;
        }
        if (strMin.contains(".")) {
            strMin = strMin.substring(0, strMin.indexOf("."));
        }

        String strHour = hour + "";
        if (hour < 10) {
            strHour = "0" + hour;
        }
        if (strHour.contains(".")) {
            strHour = strHour.substring(0, strHour.indexOf("."));
        }

        String strDay = day + "";
        if (day < 10) {
            strDay = "0" + day;
        }
        if (strDay.contains(".")) {
            strDay = strDay.substring(0, strDay.indexOf("."));
        }

        arr.add(strDay);
        arr.add(strHour);
        arr.add(strMin);
        arr.add(strSec);

        return arr;
    }

    /*
 * Get time ago
 *
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String getTimeAgo(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT_FULL_DATETIME_SERVER);
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getTimeAgo(date.getTime());
    }

}
