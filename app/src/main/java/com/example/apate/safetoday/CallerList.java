package com.example.apate.safetoday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

/**
 * Created by biancaangotti on 2016-11-12.
 */

public class CallerList extends BroadcastReceiver {
    // Two mapping fields for the database table
    private MainActivity x = new MainActivity();

    private boolean multipleCaller(ArrayList<Date> time_stamps) {
        Date last = time_stamps.get(time_stamps.size()-1);
        long t = last.getTime();
        Date afterRemovingThirtyMins = new Date(t - (30 * 60 * 1000));
        if (time_stamps.get(time_stamps.size()-4).after(afterRemovingThirtyMins)) return false;
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            Bundle bundle = intent.getExtras();
            String phoneNumber = bundle.getString("incoming_number");
            Log.d("INCOMING", phoneNumber);
            if ((phoneNumber != null)) {
                Map<String, ArrayList<Date>> map = x.getMap();
                ArrayList<Date> time_stamps = map.get(phoneNumber);
                time_stamps.add(new Date());
                map.put(phoneNumber, time_stamps);
                x.putMap(map);

                //need to check all the times and numbers now....
                // True = hang up!
                if (time_stamps.size() < 3 || multipleCaller(time_stamps)) {
                    telephonyService.endCall();
                    Log.d("HANG UP", phoneNumber);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
