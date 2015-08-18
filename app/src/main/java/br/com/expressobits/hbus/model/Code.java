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

    /**
     * Retorna identificação do código
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna descrição do código.
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
