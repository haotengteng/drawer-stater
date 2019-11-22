package com.bee.ms.drawer.utils;

import com.bee.ms.drawer.exception.ErrorCodeEnum;
import com.bee.ms.drawer.exception.ServiceException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换
 *
 * @author created by htt on 2018/9/29
 */
public class DateConvertUtil {

    private static final String date_format = "yyyy-MM-dd HH:mm:ss";
    private static final String day_format = "yyyy-MM-dd";
    private static final String minute_format = "yyyy-MM-dd HH:mm";
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal();
    private static ThreadLocal<DateFormat> threadLocalDay = new ThreadLocal();
    private static ThreadLocal<DateFormat> threadLocalMinute = new ThreadLocal();


    private static DateFormat getDateFormat() {
        DateFormat df = threadLocal.get();
        if (df == null) {
            df = new SimpleDateFormat(date_format);
            threadLocal.set(df);
        }
        return df;
    }

    private static DateFormat getDayFormat() {
        DateFormat df = threadLocalDay.get();
        if (df == null) {
            df = new SimpleDateFormat(day_format);
            threadLocalDay.set(df);
        }
        return df;
    }

    private static DateFormat getMinuteFormat() {
        DateFormat df = threadLocalMinute.get();
        if (df == null) {
            df = new SimpleDateFormat(minute_format);
            threadLocalMinute.set(df);
        }
        return df;
    }

    public static String formatDate(Date date) {
        return getDateFormat().format(date);
    }

    public static String formatDay(Date date) {
        return getDayFormat().format(date);
    }

    public static String formatMinute(Date date) {
        return getMinuteFormat().format(date);
    }

    public static Date parse(String strDate) {
        try {
            return getDateFormat().parse(strDate);
        } catch (ParseException e) {
            throw new ServiceException(ErrorCodeEnum.ERROR_ILLEGAL_PARAMETER.customMassage("日期格式错误"));
        }
    }

    public static Date parseDay(String strDay) {
        try {
            return getDayFormat().parse(strDay);
        } catch (ParseException e) {
            throw new ServiceException(ErrorCodeEnum.ERROR_ILLEGAL_PARAMETER.customMassage("日期格式错误"));
        }
    }

    public static Date parseMinute(String strDay) {
        try {
            return getMinuteFormat().parse(strDay);
        } catch (ParseException e) {
            throw new ServiceException(ErrorCodeEnum.ERROR_ILLEGAL_PARAMETER.customMassage("日期格式错误"));
        }
    }
}
