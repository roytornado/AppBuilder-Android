package com.royalnext.base.util;

import java.util.Date;
import java.util.UUID;

public class Ran {

    public static String genDeviceID() {
        return "" + UUID.randomUUID();
    }

    public static int ranInt() {
        return (int) (new Date().getTime() + (int) (Math.random() * 1000000));
    }

    public static String ranStr() {
        return "" + UUID.randomUUID();
    }

}
