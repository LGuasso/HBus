package br.com.expressobits.hbus.model;

/**
 * @author Rafael Correa
 * @since 31/07/2015
 *
 */
public class Code {
    /**
     * Identifica�ao do c�digo.
     */
    private String name;
    /**
     * Descri��o do c�digo
     */
    private String descrition;

    private City city;

    /**
     * Retorna identifica��o do c�digo
     * @return Nome
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna descri��o do c�digo.
     * @return Descri��o
     */
    public String getDescrition() {
        return descrition;
    }

    public void setName(String code) {
        this.name = code;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Code && this.getName().equals(((Code) o).getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}
