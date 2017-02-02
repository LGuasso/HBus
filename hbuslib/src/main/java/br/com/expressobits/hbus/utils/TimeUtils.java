package br.com.expressobits.hbus.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import br.com.expressobits.hbus.model.TypeDay;

/**
 * Métodos estáticos para utilidade de Tempo.
 * @author Rafael Correa
 * @since 15/06/2015
 */
public class TimeUtils {

    public static final String TAG = "TimeUtils";
    /**
     * Constante que contém a data de troca dos tempos do servidor UTC
     */
    public static final long TIME_CHANGE_TIMEZONE_TO_UTC = 1464750000000l;

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

    public static int getHour(long time){
        Calendar calendar;
        if(time<TIME_CHANGE_TIMEZONE_TO_UTC){
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        }else{
            calendar = Calendar.getInstance();
        }
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(long time){
        Calendar calendar;
        if(time<TIME_CHANGE_TIMEZONE_TO_UTC){
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        }else{
            calendar = Calendar.getInstance();
        }
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.MINUTE);
    }

    public static String getFormatTime(Calendar calendar){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(calendar.getTime());
    }

    public static String getFormatTime(long time){
        //TODO bug #032 corrigir todos horários para UTC até 01/03/2017
        //
        Calendar calendar;
        if(time<TIME_CHANGE_TIMEZONE_TO_UTC){
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
