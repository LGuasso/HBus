package br.com.expressobits.hbus.modelo;

import android.util.Log;

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

        switch (this){
            case UTEIS:
                return "uteis";
            case SABADO:
                return "sabado";
            case DOMINGO:
                return "domingo";
            default:
                Log.e("ERRO","Erro ao preencher informação de dias!");
                return "ERRO";
        }
    }

}
