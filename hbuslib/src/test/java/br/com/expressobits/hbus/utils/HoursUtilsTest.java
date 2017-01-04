package br.com.expressobits.hbus.utils;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * @author Rafael Correa
 * @since 04/01/17
 */
public class HoursUtilsTest {
    @Test
    public void getTimeInCalendar() throws Exception {
        System.out.println(HoursUtils.getTimeInCalendar("12:00").getTimeInMillis());
    }

    @Test
    public void testGetHour() throws Exception {
        long time  =  HoursUtils.getTimeInCalendar("12:00").getTimeInMillis();
        System.out.println(HoursUtils.getHour(time));
    }


}