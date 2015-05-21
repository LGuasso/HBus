package br.com.expressobits.hbus.modelo;

/**
 * Created by Rafael on 20/05/2015.
 * Define lista dos tipos de dia das linhas
 */
public enum TipoDeDia {
    /**
     * Dias úteis - Segunda á sexta
     */
    UTEIS,
    SABADO,DOMINGO;

    @Override
    public String toString() {
        if (this.equals(UTEIS)) {
            return "uteis";
        }else{
            return "NADA";
        }
    }
}
