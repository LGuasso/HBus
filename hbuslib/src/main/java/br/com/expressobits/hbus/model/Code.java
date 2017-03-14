package br.com.expressobits.hbus.model;

/**
 * @author Rafael Correa
 * @since 31/07/2015
 */
public class Code {

    public static final int CODE_LENGTH_TO_DESCRIPTION = 6;
    private String id;
    /**
     * Identificacao do codigo.
     */
    private String name;
    /**
     * Descricao do codigo
     */
    private String descrition;

    public String getId() {
        return id;
    }

    /**
     * Retorna identifica��o do c�digo
     *
     * @return Nome
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna descri��o do c�digo.
     *
     * @return Descri��o
     */
    public String getDescrition() {
        return descrition;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String code) {
        this.name = code;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Code && this.getName().toLowerCase().equals(((Code) o).getName().toLowerCase());
    }

    @Override
    public String toString() {
        return getName();
    }
}
