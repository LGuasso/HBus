package br.com.expressobits.hbus.modelo;

/**
 * Created by Rafael on 21/05/2015.
 */
public class Onibus {

    String horario;
    String codigo;


    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getHorario() {
        return horario;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getHora(){
        return Integer.parseInt(horario.split(":")[0]);
    }

    public int getMinuto(){
        return Integer.parseInt(horario.split(":")[1]);
    }


}
