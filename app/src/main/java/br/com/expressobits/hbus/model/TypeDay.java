package br.com.expressobits.hbus.model;

import android.util.Log;

/**
 * Created by Rafael on 20/05/2015.
 * Define lista dos tipos de dia das linhas
 */
public enum TypeDay {

    /**
     * Dias úteis - Segunda á sexta
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
                Log.e("ERRO","Erro ao preencher informação de dias!");
                return "ERRO";
        }
    }



}
