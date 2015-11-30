package br.com.expressobits.hbus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Correa.
 * @since 18/06/2015.
 */
public class Itinerary {


    private Long id;
    /**
     * Nome do itiner�rio.
     */
    private String name;
    /**
     * flag que define se este itiner�rio � favorito.
     */
    private boolean favorite;

    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<String> ways;


    public List<String> getWays() {
        return ways;
    }

    public void setWays(List<String> ways) {
        this.ways = ways;
    }

    private ArrayList<String> codigos;

    public ArrayList<String> getCodigos(){
        return codigos;
    }

    public void setCodigos(ArrayList<String> codigos) {
        this.codigos = codigos;
    }

}
