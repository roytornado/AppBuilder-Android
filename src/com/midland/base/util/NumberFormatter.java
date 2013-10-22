package com.midland.base.util;

import java.text.DecimalFormat;


public class NumberFormatter {
    private static final String PATTERN = "$##,###";
    public static String formatAsPrice(String number) {
        DecimalFormat format = new DecimalFormat("#,###");
        String moneyStr = format.format(Double.parseDouble(number));
        return moneyStr;
    }
}
