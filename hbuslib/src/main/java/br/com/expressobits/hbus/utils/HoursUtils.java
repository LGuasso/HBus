package br.com.expressobits.hbus.utils;

import android.util.Log;
import android.util.Pair;

import org.jetbrains.annotations.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.TypeDay;

/**
 * @author Rafael Correa
 * @since 15/06/2015
 */
public class HoursUtils {

    public static final String TAG = "TimeUtils";

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
        Calendar cal = GregorianCalendar.getInstance();
        String  time;
        time = cal.get(Calendar.HOUR_OF_DAY)+":";
        time+=cal.get(Calendar.MINUTE);
        Log.d(TAG,"Criado texto com atual hora "+time);
        return time;
    }

    /**
     * Retorna o atual horario na forma de String HH:mm
     * @return String do horario atual em HH:mm
     */
    public static Calendar getTimeInCalendar(String time){
        if(time.contains(":")){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time.split(":")[0]));
            cal.set(Calendar.MINUTE,Integer.parseInt(time.split(":")[1]));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Log.d(TAG,"Criado calendar com atual hora "+time+" = "+sdf.format(cal.getTime()));
            return cal;
        }else{
            return null;
        }

    }


    public static Pair<Integer,List<Bus>> sortByTimeBus(List<Bus> busList){

        //Collections.sort(busList);
        Bus bus = new Bus();
        bus.setTime(Calendar.getInstance().getTimeInMillis());
        List<Bus> busFinal = new ArrayList<>();
        ArrayList<Bus> twoLastBuses = new ArrayList<>();
        ArrayList<Bus> lastBuses = new ArrayList<>();
        ArrayList<Bus> nextBuses = new ArrayList<>();

        int countTwoLast;

        for (int i=0;i<busList.size();i++){
            if(getHour(busList.get(i).getTime())>getHour(bus.getTime())){
                nextBuses.add(busList.get(i));
            }else if(getHour(busList.get(i).getTime())<getHour(bus.getTime())){
                    lastBuses.add(busList.get(i));

            }else{
                if(getMinute(busList.get(i).getTime())>=getMinute(bus.getTime())){
                    nextBuses.add(busList.get(i));
                }else if(getMinute(busList.get(i).getTime())<getMinute(bus.getTime())) {
                        lastBuses.add(busList.get(i));
                }
            }
        }

        if(lastBuses.size()>1){
            Bus bus2 = lastBuses.get(lastBuses.size() - 2);
            Bus bus1 = lastBuses.get(lastBuses.size() - 1);
            twoLastBuses.add(bus2);
            lastBuses.remove(bus2);
            //Tem que ser invertido para aparecer embaixo o mais recente
            twoLastBuses.add(bus1);
            lastBuses.remove(bus1);

        }else if(lastBuses.size()>0){
            Bus bus1 = lastBuses.get(lastBuses.size() - 1);
            twoLastBuses.add(bus1);
            lastBuses.remove(bus1);
        }

        countTwoLast = twoLastBuses.size();


        for(Bus bus1 : twoLastBuses){
            busFinal.add(bus1);
        }

        for(Bus bus1 : nextBuses){
            busFinal.add(bus1);
        }

        for(Bus bus1 : lastBuses){
            busFinal.add(bus1);
        }

        return new Pair<>(countTwoLast,busFinal);
    }

    /**public static int getHour(Bus bus){
        return Integer.parseInt(bus.getTime().split(":")[0]);
    }

    public static int getMinute(Bus bus){
        return Integer.parseInt(bus.getTime().split(":")[1]);
    }
     */

    public static int getHour(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(long time){
        Calendar calendar = Calendar.getInstance();
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

    /**
     * Este metodo valida se o alarme pode ser ativado.
     * @param c
     * @param cAlarm
     * @return
     */
    public static boolean isValidAlarm(Calendar c,Calendar cAlarm,int minutesTolerados){
        if(c.compareTo(cAlarm)>-1){
            Log.d(TAG,"now calendar is actual to cAlarm!");
            cAlarm.add(Calendar.MINUTE,minutesTolerados);
            if(c.compareTo(cAlarm)<1){
                return true;
            }else{
                Log.d(TAG,"now calendar is NOT actual to cAlarm+minutesTolerance!");
            }
        }
        Log.d(TAG,"now calendar not is actual to cAlarm!");
        return false;
    }



}
