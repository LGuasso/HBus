package br.com.expressobits.hbus.modelo;

/**
 * Created by Rafael on 21/05/2015.
 */
public class Onibus {

    /**
     * Hor�rio �nico desse �nibus
     */
    String horario;
    /**
     * C�digo �nico desse �nibus
     */
    Codigo codigo;


    public void setCodigo(Codigo codigo) {
        this.codigo = codigo;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getHorario() {
        return horario;
    }

    public Codigo getCodigo() {
        return codigo;
    }

    public int getHora(){
        return Integer.parseInt(horario.split(":")[0]);
    }

    public int getMinuto(){
        return Integer.parseInt(horario.split(":")[1]);
    }


}
