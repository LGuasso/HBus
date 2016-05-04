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


    public static TypeDay getTypeday(String typeday){
        switch (typeday){
            case "useful":
                return USEFUL;
            case "saturday":
                return SATURDAY;
            case "sunday":
                return SUNDAY;
        }
        return USEFUL;
    }


    public int toInt(){
        switch (this){
            case USEFUL:
                return 0;
            case SATURDAY:
                return 1;
            case SUNDAY:
                return 2;
            default:
                return 4;
        }
    }

    @Override
    public String toString() {

        switch (this){
            case USEFUL:
                return "useful";
            case SATURDAY:
                return "saturday";
            case SUNDAY:
                return "sunday";
            default:
                return "ERRO";
        }
    }





}
