package br.com.expressobits.hbus.model;

/**
 * @author Rafael Correa
 * @since 21/05/2015.
 * Classe que modela o �nibus que passa num �nico hor�rio em �nico sentido.
 */
public class Bus implements Comparable<Bus>{



    public static final String TAG="Bus";

    /**
     * Sentido do �nibus expresso em {@link String}
     */
    private String way;

    /**
     * Nome da linha do �nibus expresso em {@link String}
     */
    private Itinerary itinerary;
    /**
     * Hor�rio do bus expresso em {@link String}
     */
    private String time;
    /**
     * Codigo �nico desse �nibus {@link Code}
     */
    private Code code;

    /**
     * Tipo do dia expresso em {@link TypeDay}
     */
    private TypeDay typeday;

    /**
     * @deprecated
     */
    private boolean tomorrow;

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

    public boolean isTomorrow(){
        return this.tomorrow;
    }

    public void setTomorrow(boolean tomorrow){
        this.tomorrow = tomorrow;
    }

    public int getHora(){
        return Integer.parseInt(time.split(":")[0]);
    }

    /**
     * Retorna minutos do hor�rio do �nibus
     * @return
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
    public int compareTo(Bus another) {

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

}
