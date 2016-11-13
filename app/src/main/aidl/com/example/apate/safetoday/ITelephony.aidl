// ITelephony.aidl
package com.example.apate.safetoday;
package com.android.internal.telephony;

// Declare any non-default types here with import statements

interface ITelephony {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
