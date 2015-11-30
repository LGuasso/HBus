package br.com.expressobits.hbus.model;

/**
 * @author Rafael Correa
 * @since 31/07/2015
 *
 */
public class Code {
    /**
     * Identificaçao do código.
     */
    private String name;
    /**
     * Descrição do código
     */
    private String descrition;

    private City city;

    /**
     * Retorna identificação do código
     * @return Nome
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna descrição do código.
     * @return Descrição
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
