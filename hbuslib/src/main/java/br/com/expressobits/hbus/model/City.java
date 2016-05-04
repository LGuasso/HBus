package br.com.expressobits.hbus.model;

/**
 * @author Rafael Correa
 * @since 18/10/15
 */
public class City {


    private String id;
    private String name;
    private String country;
    private String position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return getName()+" - "+getCountry();
    }
}
