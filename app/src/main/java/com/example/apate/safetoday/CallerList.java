package com.example.apate.safetoday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by biancaangotti on 2016-11-12.
 */

public class CallerList extends BroadcastReceiver {
    // Two mapping fields for the database table
    MainActivity x = new MainActivity();

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
                Map<Date, String> map = x.getMap();
                map.put(new Date(), phoneNumber);
                x.putMap(map);

                //need to check all the times and numbers now....

                telephonyService.endCall();
                Log.d("HANG UP", phoneNumber);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
