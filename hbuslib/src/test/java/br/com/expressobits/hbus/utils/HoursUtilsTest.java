package br.com.expressobits.hbus.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Rafael Correa
 * @since 04/01/17
 */
public class HoursUtilsTest {
    @Test
    public void getTimeInCalendar() throws Exception {
        System.out.println(TimeUtils.getTimeInCalendar("12:00").getTimeInMillis());
    }

    @Test
    public void testGetHour() throws Exception {
        long time  =  TimeUtils.getTimeInCalendar("12:00").getTimeInMillis();
        System.out.println(TimeUtils.getHour(time));
    }

    @Test
    public void testGetFormatHour() throws Exception {
        String hour = "12:00";
        long time  =  TimeUtils.getTimeInCalendar(hour).getTimeInMillis();
        if(time> TimeUtils.TIME_CHANGE_TIMEZONE_TO_UTC){
            assertNotEquals(hour, TimeUtils.getFormatTime(time));
        }else {
            assertEquals(hour, TimeUtils.getFormatTime(time));
        }



    }


}