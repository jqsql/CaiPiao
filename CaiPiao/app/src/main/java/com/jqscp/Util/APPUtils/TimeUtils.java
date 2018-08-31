package com.jqscp.Util.APPUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具
 */

public class TimeUtils {

    /**
     * 将秒转化为分秒 eg：60  -->  01:00
     * @param second
     */
    public static String setSecond2Minute(int second){
        if (second < 10) {
            return "00:0" + second;
        }
        if (second < 60) {
            return "00:" + second;
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second % 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":0" + second;
                }
                return "0" + minute + ":" + second;
            }
            if (second < 10) {
                return minute + ":0" + second;
            }
            return minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second % 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + minute + ":0" + second;
            }
            return "0" + hour + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour + minute + ":0" + second;
        }
        return hour + minute + ":" + second;
    }

    /**
     * 时间戳 转 yyyy-MM-dd HH:mm:ss
     * @param timestamp 时间戳
     * @return
     */
    public static String getTimestamp2Long(String timestamp){
        if(timestamp==null)
            return "";
        try {
            long timeStr=Long.parseLong(timestamp+"000");
            Date date = new Date(timeStr);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }catch (NumberFormatException e){
            return "";
        }
    }
    /**
     * 时间戳 转 yyyy-MM-dd
     * @param timestamp 时间戳
     * @return
     */
    public static String getTimestamp2ShortY(String timestamp){
        try {
            long timeStr=Long.parseLong(timestamp+"000");
            Date currentTime = new Date(timeStr);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(currentTime);
        }catch (NumberFormatException e){
            return "";
        }
    }
    /**
     * 时间戳 转 1月1日
     * @param timestamp 时间戳
     * @return
     */
    public static String getTimestamp2ShortStr(String timestamp){
        try {
            long timeStr=Long.parseLong(timestamp+"000");
            Date currentTime = new Date(timeStr);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String[] times=formatter.format(currentTime).split("-");
            if(times.length!=3)
                return "";
            return times[1]+"月"+times[2]+"日";
        }catch (NumberFormatException e){
            return "";
        }
    }
    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimestamp2ShortM(String timestamp) {
        try {
            long timeStr=Long.parseLong(timestamp+"000");
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date currentTime = new Date(timeStr);
            return formatter.format(currentTime);
        }catch (NumberFormatException e){
            return "";
        }

    }
    /**
     * 获取时间  MM-dd HH:mm
     *
     * @return
     */
    public static String getTimestamp2Str(String timestamp) {
        try {
            long timeStr=Long.parseLong(timestamp+"000");
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
            Date currentTime = new Date(timeStr);
            return formatter.format(currentTime);
        }catch (NumberFormatException e){
            return "";
        }

    }
    /**
     * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
     *
     * @param timestamp
     * @param num
     * @return
     */
    public static String getWeek(String timestamp, String num) {
        try {
            long timeStr=Long.parseLong(timestamp);
            Date dd = new Date(timeStr);
            Calendar c = Calendar.getInstance();
            c.setTime(dd);
            if (num.equals("1")) // 返回星期一所在的日期
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            else if (num.equals("2")) // 返回星期二所在的日期
                c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            else if (num.equals("3")) // 返回星期三所在的日期
                c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            else if (num.equals("4")) // 返回星期四所在的日期
                c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            else if (num.equals("5")) // 返回星期五所在的日期
                c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            else if (num.equals("6")) // 返回星期六所在的日期
                c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            else if (num.equals("0")) // 返回星期日所在的日期
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        }catch (NumberFormatException e){
            return "";
        }
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(long sdate) {
        Date date = new Date(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }
    public static String getWeekStr(String timestamp){
        try {
            long timeStr=Long.parseLong(timestamp);
            String str = TimeUtils.getWeek(timeStr);
            if("1".equals(str)){
                str = "星期日";
            }else if("2".equals(str)){
                str = "星期一";
            }else if("3".equals(str)){
                str = "星期二";
            }else if("4".equals(str)){
                str = "星期三";
            }else if("5".equals(str)){
                str = "星期四";
            }else if("6".equals(str)){
                str = "星期五";
            }else if("7".equals(str)){
                str = "星期六";
            }
            return str;
        }catch (NumberFormatException e){
            return "";
        }

    }
}
