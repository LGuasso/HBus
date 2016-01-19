package br.com.expressobits.hbus.model;


/**
 * Created by Rafael on 20/05/2015.
 * Define lista dos tipos de dia das linhas
 */
public enum TypeDay {

    /**
     * Dias Ùteis - Segunda á sexta
     */
    USEFUL,
    SATURDAY,SUNDAY;

    @Override
    public String toString() {

        switch (this){
            case USEFUL:
                return "uteis";
            case SATURDAY:
                return "sabado";
            case SUNDAY:
                return "domingo";
            default:
                return "ERRO";
        }
    }



}
