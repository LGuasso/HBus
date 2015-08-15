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





}
