package com.sahni.rahul.iitdair.Helper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ContentUtils {

    private static SimpleDateFormat minuteFormatter = new SimpleDateFormat("m", Locale.getDefault());
    private static SimpleDateFormat hourFormatter = new SimpleDateFormat("h", Locale.getDefault());
    private static SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private static SimpleDateFormat dateWithTimeFormatter = new SimpleDateFormat("dd MMM, hh a", Locale.getDefault());

    public static String getProperDate(String date){
        //        2018-04-20T18:52:29.408Z"
        date = date.replace("T", " ");
        date = date.replace("Z", "");
        Log.d("Rahul", "date ="+date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        SimpleDateFormat minuteFormatter = new SimpleDateFormat("m");
        minuteFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat hourFormatter = new SimpleDateFormat("h");
        hourFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long originalTime  = formatter.parse(date).getTime();
            long currentTime = System.currentTimeMillis();
            if(currentTime - originalTime <= 60000){
                return minuteFormatter.format(new Date(currentTime - originalTime)) +" minutes ago";
            } else {
                return hourFormatter.format(new Date(currentTime - originalTime)) +" hours ago";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Not available";
        }
    }

    public static String epochToLastUpdated(long epoch){
        minuteFormatter = new SimpleDateFormat("m", Locale.getDefault());
        hourFormatter = new SimpleDateFormat("h", Locale.getDefault());
        SimpleDateFormat dayFormatter = new SimpleDateFormat("d", Locale.getDefault());
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - epoch;
        if(difference <= 60000){
            return minuteFormatter.format(new Date(difference)) +" minutes ago";
        } else if(difference <= 24*60*60*1000) {
            return hourFormatter.format(new Date(difference)) +" hours ago";
        } else {
            String days = dayFormatter.format(new Date(difference));
            return days.equals("1") ? days + " day ago" : days + " days ago";
        }
//        Date date = new Date();
//        date.getTime()
    }

    public static String epochToMonth(long epoch){
        return monthFormatter.format(new Date(epoch));
    }

    public static String epochToDate(long epoch){
        return dateFormatter.format(new Date(epoch));
    }

    public static String epochToDateTime(long epoch){
        return dateWithTimeFormatter.format(new Date(epoch));
    }
}
