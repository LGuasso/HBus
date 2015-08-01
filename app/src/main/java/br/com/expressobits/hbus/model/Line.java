package br.com.expressobits.hbus.model;

import java.util.ArrayList;
import java.util.Collections;

import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * Created by Rafael on 20/05/2015.
 */
public class Line {

    private int id;

    private String nome;
    /**
     * Define o sentido da linha
     * <n>false</n> para bairro-centro
     * <n>true</n> para centro-bairro
     */
    private boolean sentido;
    /**
     * Define o dia da linha
     * @see TypeDay
     */
    private TypeDay dia;


    ArrayList<Bus> onibuses;


    public void setOnibuses(ArrayList<Bus> onibuses) {
        this.onibuses = onibuses;
    }

    public ArrayList<Bus> getOnibuses() {
        return onibuses;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isSentido() {
        return sentido;
    }

    public void setSentido(boolean sentido) {
        this.sentido = sentido;
    }

    public TypeDay getDia() {
        return dia;
    }

    public void setDia(TypeDay dia) {
        this.dia = dia;
    }


    public static ArrayList<Bus> sortBusperHour(ArrayList<Bus> onibuses){

        Collections.sort(onibuses);

        Bus bus = new Bus();
        bus.setTime(TimeUtils.getNowTimeinString());
        ArrayList<Bus> busFinal = new ArrayList<>();
        ArrayList<Bus> varFinal = new ArrayList<>();
        for (int i=0;i<onibuses.size();i++){
            if(onibuses.get(i).getHora()>bus.getHora()){
                busFinal.add(onibuses.get(i));
            }else if(onibuses.get(i).getHora()<bus.getHora()){
                varFinal.add(onibuses.get(i));
            }else{
                if(onibuses.get(i).getMinutos()>=bus.getMinutos()){
                    busFinal.add(onibuses.get(i));
                }else if(onibuses.get(i).getMinutos()<bus.getMinutos()){
                    varFinal.add(onibuses.get(i));
                }
            }
        }
        for(Bus bus1 : varFinal){
            busFinal.add(bus1);
        }

        return busFinal;

    }




}
