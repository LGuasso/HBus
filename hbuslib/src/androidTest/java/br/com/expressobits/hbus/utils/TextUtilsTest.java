package br.com.expressobits.hbus.utils;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Created by rafael on 03/04/16.
 */
public class TextUtilsTest extends TestCase {

    public void testGetTimeWIthDelayTime() throws Exception {
        String time = "00:05";
        int delay = -15;
        String result = TextUtils.getTimeStringWithDelayTime(time, delay);
        Log.d("Test",result);
        assertEquals("23:50",result);
    }
}