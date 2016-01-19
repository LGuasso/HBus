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
     * Nome do itinerário.
     */
    private String name;

    public List<String> ways;

    private ArrayList<String> codigos;

    private Long cityid;

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

    public List<String> getWays() {
        return ways;
    }

    public void setWays(List<String> ways) {
        this.ways = ways;
    }

    public void setCityid(Long cityid) {
        this.cityid = cityid;
    }

    public Long getCityid() {
        return cityid;
    }

    public ArrayList<String> getCodigos(){
        return codigos;
    }

    public void setCodigos(ArrayList<String> codigos) {
        this.codigos = codigos;
    }

}
