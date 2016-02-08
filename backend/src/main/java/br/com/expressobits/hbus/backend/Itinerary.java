package br.com.expressobits.hbus.backend;

import java.util.List;

/**
 * Model of itinerary in cloud datastore
 * @author Rafael
 * @since 08/02/16
 */
public class Itinerary {

    private String id;

    private String name;

    private List<String> ways;

    private List<String> codes;

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

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
