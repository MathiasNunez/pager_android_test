package com.mnunez.pagertest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mnunez on 5/26/17.
 */

public class DateUtils {

    public static String getStringFromDate(Date date, String format) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            try {
                dateString = simpleDateFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateString;
    }

    public static Date getDateFromString(String s_date, String format) {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
            date = simpleDateFormat.parse(s_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
