package br.com.expressobits.hbus.model;

import android.support.annotation.NonNull;

/**
 * @author Rafael Correa
 * @since 21/05/2015.
 * Classe que modela o õnibus que passa num único horário em único sentido.
 */
public class Bus implements Comparable<Bus>{



    public static final String TAG="Bus";

    /**
     * Sentido do ônibus expresso em {@link String}
     */
    private String way;

    /**
     * Nome da linha do ônibus expresso em {@link String}
     */
    private Itinerary itinerary;
    /**
     * Horário do bus expresso em {@link String}
     */
    private String time;
    /**
     * Codigo único desse ônibus {@link Code}
     */
    private Code code;

    /**
     * Tipo do dia expresso em {@link TypeDay}
     */
    private TypeDay typeday;

    public void setTime(String horario) {
        this.time = horario;
    }

    public String getTime() {
        return time;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public int getHora(){
        return Integer.parseInt(time.split(":")[0]);
    }

    /**
     * Retorna minutos do horário do ônibus
     * @return Inteiro
     */
    public int getMinutos(){
        return Integer.parseInt(time.split(":")[1]);
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public TypeDay getTypeday() {
        return typeday;
    }

    public void setTypeday(TypeDay typeday) {
        this.typeday = typeday;
    }

    @Override
    public int compareTo(@NonNull Bus another) {

        if(this.getHora()>another.getHora()){
            return 1;
        }else if(this.getHora()<another.getHora()){
            return -1;
        }else{
            if(this.getMinutos()>another.getMinutos()){
                return 1;
            }else if(this.getMinutos()<another.getMinutos()){
                return -1;
            }else{
                return 0;
            }
        }
    }

    @Override
    public String toString() {
        return getTime()+" - "+getCode()+" - "+getItinerary().getName()
                +" - "+getTypeday()+" - "+getWay();
    }
}
