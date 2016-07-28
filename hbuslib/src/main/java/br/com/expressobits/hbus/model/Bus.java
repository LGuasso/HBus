package br.com.expressobits.hbus.model;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Rafael Correa
 * @since 21/05/2015.
 * Classe que modela o onibus que passa num único horario em unico sentido.
 */
public class Bus implements Comparable<Bus>{



    public static final String TAG="Bus";

    private String id;

    /**
     * Sentido do ônibus expresso em {@link String}
     */
    private String way;
    /**
     * Horário do bus expresso em {@link String}
     */
    private long time;
    /**
     * Codigo único desse ônibus {@link Code}
     */
    private String code;

    /**
     * Tipo do dia expresso em {@link TypeDay}
     */
    private String typeday;


    public String getId() {
        return id;
    }

    public void setTime(long horario) {
        this.time = horario;
    }

    public long getTime() {
        return time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getTypeday() {
        return typeday;
    }

    public void setTypeday(String typeday) {
        this.typeday = typeday;
    }


    @Override
    public int compareTo(Bus another) {

        Calendar thisCalendar = Calendar.getInstance();
        Calendar anotherCalendar = Calendar.getInstance();
        thisCalendar.setTimeInMillis(this.getTime());
        anotherCalendar.setTimeInMillis(another.getTime());

        int hourThis = thisCalendar.get(Calendar.HOUR_OF_DAY);
        int hourAnother = anotherCalendar.get(Calendar.HOUR_OF_DAY);
        int minuteThis = thisCalendar.get(Calendar.MINUTE);
        int minuteAnother = anotherCalendar.get(Calendar.MINUTE);

        if(hourThis>hourAnother){
            return 1;
        }else if(hourThis<hourAnother){
            return -1;
        }else{
            if(minuteThis>minuteAnother){
                return 1;
            }else if(minuteThis<minuteAnother){
                return -1;
            }else{
                return 0;
            }
        }
    }

    /**@Override
    public int compareToOld(Bus another) {

        int hourThis = Integer.parseInt(this.time.split(":")[0]);
        int hourAnother = Integer.parseInt(another.time.split(":")[0]);
        int minuteThis = Integer.parseInt(this.time.split(":")[1]);
        int minuteAnother = Integer.parseInt(another.time.split(":")[1]);

        if(hourThis>hourAnother){
            return 1;
        }else if(hourThis<hourAnother){
            return -1;
        }else{
            if(minuteThis>minuteAnother){
                return 1;
            }else if(minuteThis<minuteAnother){
                return -1;
            }else{
                return 0;
            }
        }
    }
*/
    @Override
    public String toString() {
        return getTime()+" - "+getCode()+" - "
                +" - "+getTypeday()+" - "+getWay();
    }
}
