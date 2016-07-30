package br.com.expressobits.hbus.utils;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by rafael on 28/07/16.
 */
public class HoursUtilsTest extends TestCase {

    @Test
    public void testIsValidAlarm() throws Exception {
        Calendar c = Calendar.getInstance();
        Calendar cAlarm = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY,17);
        c.set(Calendar.MINUTE,57);

        cAlarm.set(Calendar.HOUR_OF_DAY,18);
        cAlarm.set(Calendar.MINUTE,17);

        assertTrue(HoursUtils.isValidAlarm(c,cAlarm,30));
    }
}