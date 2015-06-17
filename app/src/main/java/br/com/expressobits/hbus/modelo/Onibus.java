package br.com.expressobits.hbus.modelo;

import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * Created by Rafael on 21/05/2015.
 */
public class Onibus implements Comparable<Onibus>{

    public static final String TAG="modelo";

    /**
     * Horário do bus
     */
    private Calendar time;
    /**
     * Código único desse ônibus
     */
    private Codigo codigo;

    private boolean tomorrow;

    public void setTime(Calendar horario) {
        this.time = horario;
    }

    public Calendar getTime() {
        return time;
    }

    public void setCodigo(Codigo codigo) {
        this.codigo = codigo;
    }

    public Codigo getCodigo() {
        return codigo;
    }

    public boolean isTomorrow(){
        return this.tomorrow;
    }

    public void setTomorrow(boolean tomorrow){
        this.tomorrow = tomorrow;
    }

    @Override
    public int compareTo(Onibus another) {
        Log.d(TAG, "C1-DIA" + this.getTime().get(Calendar.DAY_OF_MONTH) + " "
                + this.getTime().get(Calendar.HOUR_OF_DAY) + ":" + this.getTime().get(Calendar.MINUTE));
        Log.d(TAG, "C2-DIA"+another.getTime().get(Calendar.DAY_OF_MONTH)+" "
                +another.getTime().get(Calendar.HOUR_OF_DAY)+":"+another.getTime().get(Calendar.MINUTE));
        Calendar now = GregorianCalendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -1);
        Log.d(TAG, "A hora do compareTo now é" + now.get(Calendar.HOUR_OF_DAY));
        Calendar c1 =  (Calendar)this.getTime().clone();
        c1.set(Calendar.DAY_OF_MONTH,GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH));
        Calendar c2 = (Calendar)another.getTime().clone();
        c2.set(Calendar.DAY_OF_MONTH,GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH));


        if(now.get(Calendar.HOUR_OF_DAY)>c1.get(Calendar.HOUR_OF_DAY)){
            Log.d(TAG, "now vem depois de c1");
            c1.add(Calendar.DAY_OF_MONTH, +1);
            setTomorrow(true);
        }

        if(now.get(Calendar.HOUR_OF_DAY)>c2.get(Calendar.HOUR_OF_DAY)){
            Log.d(TAG, "now vem depois de c2");
            c2.add(Calendar.DAY_OF_MONTH,+1);
        }

        if(c1.after(c2)){
            return 1;
        }else{
            if(c2.after(c1)){
                return -1;
            }else{
                return 0;
            }
        }





    }
}
