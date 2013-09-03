package com.midland.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static SimpleDateFormat dateF = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat dateTimeF = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    public static SimpleDateFormat cardExpiryF = new SimpleDateFormat("MM/yy");

    public static String TIME_FORMAT = "yyyyMMddHHmmss";

    public static String format(String src) {
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT);
        Date target;
        try {
            target = df.parse(src);
        } catch (ParseException e) {
            return "";
        }
        return dateTimeF.format(target);
    }

}
