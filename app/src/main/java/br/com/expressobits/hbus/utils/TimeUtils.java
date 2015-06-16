package br.com.expressobits.hbus.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Rafael on 15/06/2015.
 */
public class TimeUtils {

    private static String format = "HH:mm";

    public static final String TAG = "TimeUtils";

    /**
     * Converte o horário no formato {@link Calendar} em {@link String}
     * @param time
     * @return
     */
    public static Calendar stringToTimeCalendar(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(sdf.parse(time));
            Log.d(TAG,"Parser String="+time+" Calendar="+cal.toString());
            return cal;
        } catch (ParseException e) {
            Log.e(TAG,"Erro ao fazer o parser de time "+time);
            return null;
        }

    }

    /**
     * Converte o horário no formato {@link String} em {@link Calendar}
     * @param time
     * @return
     */
    public static String calendarToTimeString(Calendar time){
        SimpleDateFormat s = new SimpleDateFormat(format);
        String a = s.format(time.getTime());

        return a;
    }


    /**
     * Retorna um inteiro referente ao tipo de dia da semana
     * @param calendar
     * @return
     */
    public static int getTipoDeDia(Calendar calendar){
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SATURDAY:
                return 1;
            case Calendar.SUNDAY:
                return 2;
            default:
                return 0;
        }
    }
}
