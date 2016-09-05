package br.com.expressobits.hbus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Correa.
 * @since 18/06/2015.
 */
public class Itinerary {


    private String id;
    /**
     * Nome do itinerário.
     */
    private String name;

    public List<String> ways;

    private ArrayList<String> codes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ArrayList<String> getCodes(){
        return codes;
    }

    public void setCodes(ArrayList<String> codes) {
        this.codes = codes;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
