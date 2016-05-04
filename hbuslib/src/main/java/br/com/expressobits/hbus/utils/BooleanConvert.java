package br.com.expressobits.hbus.utils;

import android.util.Log;

/**
 * @author Rafael Correa
 * @since 03/04/16
 */
public class BooleanConvert {

    public static boolean IntegerToBoolean(Integer i){
        if(i==1){
            return true;
        }else if(i==0){
            return false;
        }else {
            Log.e("BooleanType","ALGO DE ERRADO!!!!  "+(i));
            return false;
        }
    }
}
