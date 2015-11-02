package br.com.expressobits.hbus.utils;

import java.util.List;

import br.com.expressobits.hbus.model.Sentido;

/**
 * Created by rafael on 20/10/15.
 */
public class TextUtils {

    public static String getUnderlineText(String text){
        return text.replaceAll(" ", "_");
    }

    public static String getNoCaps(String text){
        return text.toLowerCase();
    }

    /**
     * Retorna nome sem espaco sem acentos e sem maiusculas.
     * @param name
     * @return
     */
    public static String toSimpleName(String name){
        if(name == null){
            return "";
        }else {
            String ok = name;
            ok = ok.replace(" > ","-");
            ok = ok.replace(" < ","-");
            ok = ok.replace(" ", "-");
            ok = ok.replace("?", "e");
            ok = ok.replace(",", "_");
            ok = ok.toLowerCase();
            return ok;
        }
    }

    public static String toSimpleNameFile(String name){
        if(name == null){
            return "";
        }else {
            String ok = name;
            ok = ok.replace(" - ","_");
            ok = ok.replace(" ","_");
            ok = ok.toLowerCase();
            return ok;
        }
    }
}
