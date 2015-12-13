package br.com.expressobits.hbus.model;

/**
 * @author Rafael Correa
 * @since 21/05/2015.
 * Classe que modela o �nibus que passa num �nico hor�rio em �nico sentido.
 */
public class Bus implements Comparable<Bus>{



    public static final String TAG="Bus";

    private Long id;

    /**
     * Sentido do �nibus expresso em {@link String}
     */
    private String way;

    /**
     * Nome da linha do �nibus expresso em {@link String}
     */
    private Long itineraryId;
    /**
     * Hor�rio do bus expresso em {@link String}
     */
    private String time;
    /**
     * Codigo �nico desse �nibus {@link Code}
     */
    private Long codeId;

    /**
     * Tipo do dia expresso em {@link TypeDay}
     */
    private TypeDay typeday;

    /**
     * Cidade onde esse �nibus passa
     */
    private Long cityid;

    public Long getId() {
        return id;
    }

    public void setTime(String horario) {
        this.time = horario;
    }

    public String getTime() {
        return time;
    }

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public int getHora(){
        return Integer.parseInt(time.split(":")[0]);
    }

    /**
     * Retorna minutos do hor�rio do �nibus
     * @return Inteiro
     */
    public int getMinutos(){
        return Integer.parseInt(time.split(":")[1]);
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCityid() {
        return cityid;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public void setCityid(Long cityid) {
        this.cityid = cityid;
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

    @Override
    public String toString() {
        return getTime()+" - "+getCodeId()+" - "+getItineraryId()
                +" - "+getTypeday()+" - "+getWay();
    }
}