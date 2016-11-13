package com.example.apate.safetoday;
import java.util.Date;
import java.util.Map;

/**
 * Created by biancaangotti on 2016-11-12.
 */

public class CallerList {
    // Two mapping fields for the database table

    // To easily create Blacklist object, an alternative constructor
    public CallerList(String phone, Map<Date, String> map) {
        map.put(new Date(), phone);
    }

}
