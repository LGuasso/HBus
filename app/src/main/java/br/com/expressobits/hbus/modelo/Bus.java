package br.com.expressobits.hbus.modelo;

/**
 * Created by Rafael on 21/05/2015.
 */
public class Bus implements Comparable<Bus>{

    public static final String TAG="modelo";

    /**
     * Horário do bus
     */
    private String time;
    /**
     * Código único desse ônibus
     */
    private Codigo codigo;

    private boolean tomorrow;

    public void setTime(String horario) {
        this.time = horario;
    }

    public String getTime() {
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

    public int getHora(){
        return Integer.parseInt(time.split(":")[0]);
    }

    public int getMinutos(){
        return Integer.parseInt(time.split(":")[1]);
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
