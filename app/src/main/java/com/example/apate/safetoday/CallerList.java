package com.example.apate.safetoday;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by biancaangotti on 2016-11-12.
 */

public class CallerList {
    Map<Date, String> map = new HashMap<Date, String>();
    // Two mapping fields for the database table

    // To easily create Blacklist object, an alternative constructor
    public CallerList(String phone) {
        map.put(new Date(), phone);
    }


}
