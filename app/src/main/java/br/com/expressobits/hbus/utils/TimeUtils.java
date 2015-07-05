package br.com.expressobits.hbus.utils;

import android.content.Intent;
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

        String a = new String("");
        if(time!=null) {
            SimpleDateFormat s = new SimpleDateFormat(format);
            a = s.format(time.getTime());
        }

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

    /**
     * Retorna o atual horário na forma de String HH:mm
     * @return String do horário atual em HH:mm
     */
    public static String getNowTimeinString(){
        Calendar cal = GregorianCalendar.getInstance();
        String  time;
        time = cal.get(Calendar.HOUR_OF_DAY)+":";
        time+=cal.get(Calendar.MINUTE);
        Log.d(TAG,"Criado texto com atual hora "+time);
        return time;
    }


    public static String getFaltaparaHorario(String timeBus){
        String timeNow = getNowTimeinString();
        int horaNow = Integer.parseInt(timeNow.split(":")[0]);
        int horaBus = Integer.parseInt(timeBus.split(":")[0]);
        int i = horaBus - horaNow;
        String texto= new String();
        if(i>0){
            texto+="Em "+i+" hora(s)";
        }else if(i<0){
            i+=24;//SE HORARIO FOR NEGATIVO SIGNIFICA QUE SO DAQUI 24HORAS //TODO CODE VER ERRRO DE DIAS  DA SEMANA
        }else{
            //NADA
        }




        return texto;
    }
}
