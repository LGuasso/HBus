package br.com.expressobits.hbus.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import br.com.expressobits.hbus.model.TypeDay;

/**
 * @author Rafael Correa
 * @since 15/06/2015
 */
public class HoursUtils {

    public static final String TAG = "TimeUtils";
    public static final long TIME_CHANGE_TIMEZONE_TO_UTC = 1464750000000l;

    /**
     * Converte o horario no formato {@link Calendar} em {@link String}
     * @param time Tempo
     * @return Calendario

    public static Calendar stringToTimeCalendar(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(sdf.parse(time));
            Log.d(TAG,"Parser String="+time+" Calendar="+cal.toString());
            return cal;
        } catch (ParseException e) {
            Log.e(TAG,"Erro ao fazer o parser de time "+time);
            return null;
        }

    }*/

    /**
     * Converte o horario no formato {@link String} em {@link Calendar}
     * @param time
     * @return

    public static String calendarToTimeString(Calendar time){

        String a = new String("");
        if(time!=null) {
            SimpleDateFormat s = new SimpleDateFormat(format);
            a = s.format(time.getTime());
        }

        return a;
    }*/


    /**
     * Retorna um inteiro referente ao tipo de dia da semana
     * @param calendar Calendar
     * @return inteiro
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

    /**
     * Retorna um inteiro referente ao tipo de dia da semana
     * @param calendar Calendario
     * @return TypeDay
     */
    public static TypeDay getTypedayinCalendar(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                return TypeDay.SATURDAY;
            case Calendar.SUNDAY:
                return TypeDay.SUNDAY;
            default:
                return TypeDay.USEFUL;
        }
    }

    /**
     * Retorna o atual horario na forma de String HH:mm
     * @return String do horario atual em HH:mm
     */
    public static String getNowTimeinString(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        String  time;
        time = calendar.get(Calendar.HOUR_OF_DAY)+":";
        time+=calendar.get(Calendar.MINUTE);
        return time;
    }

    /**
     * Retorna o atual horario na forma de String HH:mm
     * @return String do horario atual em HH:mm
     */
    public static Calendar getTimeInCalendar(String time){
        if(time.contains(":")){
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time.split(":")[0]));
            calendar.set(Calendar.MINUTE,Integer.parseInt(time.split(":")[1]));
            return calendar;
        }else{
            return null;
        }

    }




    /**public static int getHour(Bus bus){
        return Integer.parseInt(bus.getTime().split(":")[0]);
    }

    public static int getMinute(Bus bus){
        return Integer.parseInt(bus.getTime().split(":")[1]);
    }
     */

    public static int getHour(long time){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(long time){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.MINUTE);
    }

    public static String longTimetoString(long millis){
        return String.format(Locale.getDefault(),"%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public static String getFormatTime(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(calendar.getTime());
    }

    public static String getFormatTime(long time){
        //TODO bug #032 corrigir todos horários para UTC até 01/03/2017
        //
        Calendar calendar;
        if(time>TIME_CHANGE_TIMEZONE_TO_UTC){
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        }else{
            calendar = Calendar.getInstance();
        }
        calendar.setTimeInMillis(time);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(calendar.getTimeZone());
        return sdf.format(calendar.getTimeInMillis());
    }

    /**
     * Este metodo valida se o alarme pode ser ativado.
     * @param c
     * @param cAlarm
     * @return
     */
    public static boolean isValidAlarm(Calendar c,Calendar cAlarm,int minutesTolerados){
        if(c.compareTo(cAlarm)>-1){
            cAlarm.add(Calendar.MINUTE,minutesTolerados);
            if(c.compareTo(cAlarm)<1){
                return true;
            }else{
            }
        }
        return false;
    }



}
