package com.example.apate.safetoday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.os.Bundle;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by biancaangotti on 2016-11-12.
 */

public class CallerList extends BroadcastReceiver {
    // Two mapping fields for the database table

    private boolean multipleCaller(ArrayList<Date> time_stamps) {
        Date last = time_stamps.get(time_stamps.size()-1);
        long t = last.getTime();
        Date afterRemovingThirtyMins = new Date(t - (30 * 60 * 1000));
        if (time_stamps.get(time_stamps.size() - 3).after(afterRemovingThirtyMins))
            return false;
        return true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive entered");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(tm);
            Bundle bundle = intent.getExtras();
            //String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String phoneNumber = bundle.getString("incoming_number");
            Log.d("INCOMING", phoneNumber);

            if ((phoneNumber != null)) {
                System.out.println("there is a phone number: "+phoneNumber);
                Map<String, ArrayList<Date>> map = MainActivity.getMap();
                ArrayList<Date> time_stamps = new ArrayList<Date>();
                if (map.containsKey(phoneNumber)) {
                    time_stamps = map.get(phoneNumber);
                }
                Date stamp = new Date();
                // create a buffer region because of double calling
                if (time_stamps.size() > 0 && time_stamps.get(time_stamps.size()-1).before(new Date(stamp.getTime() - 2000))) {
                    time_stamps.add(stamp);
                    map.put(phoneNumber, time_stamps);
                    MainActivity.putMap(map);
                }
                else if (time_stamps.size() == 0) {
                    time_stamps.add(stamp);
                    map.put(phoneNumber, time_stamps);
                    MainActivity.putMap(map);
                }

                System.out.println(map);

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
