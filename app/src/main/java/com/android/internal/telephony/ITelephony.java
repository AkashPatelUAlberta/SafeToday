package com.android.internal.telephony;

/**
 * Created by biancaangotti on 2016-11-12.
 */

public interface ITelephony {
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}
