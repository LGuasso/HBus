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

    /**
     * Retorna identifica��o do c�digo
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna descri��o do c�digo.
     * @return
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

    @Override
    public boolean equals(Object o) {
        if(o instanceof Code){
            if(this.getName().equals(((Code)o).getName())){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }
}
