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
    private String code;
    /**
     * Descri��o do c�digo
     */
    private String descrition;

    /**
     * Retorna identifica��o do c�digo
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * Retorna descri��o do c�digo.
     * @return
     */
    public String getDescrition() {
        return descrition;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Code){
            if(this.getCode().equals(((Code)o).getCode())){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getCode();
    }
}
