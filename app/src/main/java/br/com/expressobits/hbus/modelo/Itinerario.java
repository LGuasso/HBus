package br.com.expressobits.hbus.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * Created by Rafael on 18/06/2015.
 */
public class Itinerario {

    public String nome;
    public ArrayList<Linha> linhas;
    public List<String> sentidos;


    public List<String> getSentidos() {
        return sentidos;
    }

    public void setSentidos(List<String> sentidos) {
        this.sentidos = sentidos;
    }

    private ArrayList<String> codigos;

    public ArrayList<String> getCodigos(){
        return codigos;
    }

    public void setCodigos(ArrayList<String> codigos) {
        this.codigos = codigos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Linha> getLinhas() {
        return linhas;
    }

    public void setLinhas(ArrayList<Linha> linhas) {
        this.linhas = linhas;
    }



    public ArrayList<Bus> getNextBuses(){
        ArrayList<Bus> next = new ArrayList<Bus>();
        int dia = TimeUtils.getTipoDeDia(GregorianCalendar.getInstance());
        for(int j = 0+dia;j<linhas.size();j+=3) {
            next.add(getNextBus(linhas.get(j)));
        }
        return next;
    }

    private Bus getNextBus(Linha linha){
        //TODO ver quando é domingo e o próximo onibus sera o do de segunda

        Bus nextBus;
        Bus nowBus = new Bus();

        nowBus.setTime(TimeUtils.getNowTimeinString());
        if(linha.getOnibuses().size() > 0) {
            nextBus = linha.getOnibuses().get(0);
            for (int i = 0; i < linha.getOnibuses().size(); i++) {
                nextBus = linha.getOnibuses().get(i);
                if (nowBus.compareTo(nextBus) <= 0) {
                    nextBus = linha.getOnibuses().get(i);
                    return nextBus;
                } else {

                }
            }
            return nextBus;
        }else{
            nowBus.setTime("TODO SEM ONIBUS DO DIA ");
            return nowBus;
        }
    }

}
