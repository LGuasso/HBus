package br.com.expressobits.hbus.model;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael Correa.
 * @since 18/06/2015.
 */
public class Itinerary {

    /**
     * Nome do itinerário.
     */
    private String name;
    /**
     * flag que define se este itinerário é favorito.
     */
    private boolean favorite;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getFavorite(){
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * @deprecated
     */
    public ArrayList<Line> lines;
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



    /**
     * @deprecated
     * @return
     */
    public ArrayList<Line> getLines() {
        return lines;
    }

    /**
     * @deprecated
     * @param lines
     */
    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }


    /**
     * @deprecated
     * @return
     */
    public ArrayList<Bus> getNextBuses(){
        ArrayList<Bus> next = new ArrayList<Bus>();
        int dia = TimeUtils.getTipoDeDia(GregorianCalendar.getInstance());
        for(int j = 0+dia;j< lines.size();j+=3) {
            next.add(getNextBus(lines.get(j)));
        }
        return next;
    }

    private Bus getNextBus(Line line){
        //TODO ver quando é domingo e o próximo onibus sera o do de segunda

        Bus nextBus;
        Bus nowBus = new Bus();

        nowBus.setTime(TimeUtils.getNowTimeinString());
        if(line.getOnibuses().size() > 0) {
            nextBus = line.getOnibuses().get(0);
            for (int i = 0; i < line.getOnibuses().size(); i++) {
                nextBus = line.getOnibuses().get(i);
                if (nowBus.compareTo(nextBus) <= 0) {
                    nextBus = line.getOnibuses().get(i);
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
