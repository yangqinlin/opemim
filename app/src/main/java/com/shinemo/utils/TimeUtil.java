package com.shinemo.utils;

import android.content.res.Resources;

import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.ApplicationContext;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by yangqinlin on 17/3/7.
 */

public class TimeUtil {

    public static final long INTERNEL_TIME = 1000 * 60;

    public static String getSimpleDateString(long targetDate) {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(targetDate);
        int targetYear = calendar.get(Calendar.YEAR);
        int targetMonth = calendar.get(Calendar.MONTH) + 1;
        int targetDay = calendar.get(Calendar.DAY_OF_MONTH);

        int targetHour = calendar.get(Calendar.HOUR_OF_DAY);
        String hour = targetHour >= 10 ? String.valueOf(targetHour) : "0" + targetHour;
        int targetMinute = calendar.get(Calendar.MINUTE);
        String minute = targetMinute >= 10 ? String.valueOf(targetMinute) : "0" + targetMinute;

        if (currentYear == targetYear && currentMonth == targetMonth && currentDay == targetDay) {  //今天
            return hour + ":" + minute;
        }
        Resources resource = ApplicationContext.getInstance().getResources();
        if (isYestoday(now, targetYear, targetMonth - 1, targetDay)) { //昨天
            return resource.getString(R.string.yestoday);
        }

        if (isInWeek(now, targetYear, targetMonth - 1, targetDay)) {  //一周之内
            int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
            String week = resource.getStringArray(R.array.full_week)[dayOfweek - 1];
            return week;
        }
        String month = targetMonth >= 10 ? String.valueOf(targetMonth) : "0" + targetMonth;
        String day = targetDay >= 10 ? String.valueOf(targetDay) : "0" + targetDay;
        if (currentYear == targetYear) {
            return month + "/" + day;
        }
        return String.valueOf(targetYear) + "/" + month + "/" + day;
    }


    public static String getDetailDateString(long targetDate) {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(targetDate);
        int targetYear = calendar.get(Calendar.YEAR);
        int targetMonth = calendar.get(Calendar.MONTH) + 1;
        int targetDay = calendar.get(Calendar.DAY_OF_MONTH);

        int targetHour = calendar.get(Calendar.HOUR_OF_DAY);
        String hour = targetHour >= 10 ? String.valueOf(targetHour) : "0" + targetHour;
        int targetMinute = calendar.get(Calendar.MINUTE);
        String minute = targetMinute >= 10 ? String.valueOf(targetMinute) : "0" + targetMinute;

        if (currentYear == targetYear && currentMonth == targetMonth && currentDay == targetDay) {  //今天
            return hour + ":" + minute;
        }
        Resources resource = ApplicationContext.getInstance().getResources();
        if (isYestoday(now, targetYear, targetMonth - 1, targetDay)) { //昨天
            return resource.getString(R.string.yestoday) + " " + hour + ":" + minute;
        }

        if (isInWeek(now, targetYear, targetMonth - 1, targetDay)) {  //一周之内
            int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
            String week = resource.getStringArray(R.array.full_week)[dayOfweek - 1];
            return week + " " + hour + ":" + minute;
        }
        String month = targetMonth >= 10 ? String.valueOf(targetMonth) : "0" + targetMonth;
        String day = targetDay >= 10 ? String.valueOf(targetDay) : "0" + targetDay;
        if (currentYear == targetYear) {
            return month + "/" + day + " " + hour + ":" + minute;
        }
        return String.valueOf(targetYear) + "/" + month + "/" + day + " " + hour + ":" + minute;
    }

    public static boolean isYestoday(long now, int year, int month, int day) {
        Calendar calendar = getCalByDefTZ();
        calendar.set(year, month, day);
        long yestoday = calendar.getTimeInMillis();
        long diff = now - yestoday;
        if (diff > 0 && diff <= 86400000) {
            return true;
        }
        return false;
    }

    public static boolean isInWeek(long now, int year, int month, int day) {
        Calendar calendar = getCalByDefTZ();
        calendar.set(year, month, day);
        long yestoday = calendar.getTimeInMillis();
        long diff = now - yestoday;
        if (diff > 86400000 && diff <= 86400000 * 6) {
            return true;
        }
        return false;
    }

    public static boolean isCloseEnough(long time1, long time2) {
        if (time1 - time2 < INTERNEL_TIME) {
            return true;
        } else {
            return false;
        }
    }

    public static Calendar getCalByDefTZ() {
        return Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
