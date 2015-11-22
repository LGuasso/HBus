package br.com.expressobits.hbus.utils;

import java.util.List;

/**
 * @author Rafael Correa
 * @since 20/10/15
 */
public class TextUtils {

    public static String getSentidosinString(List<String> sentidos){
        String result = "";
        for(int i=0;i<sentidos.size();i++){
            String s = sentidos.get(i);
            result+=s;
            if(i!=sentidos.size()-1){
                result+=",";
            }
        }
        return result;
    }

    /**
     * Retorna nome sem espaco sem acentos e sem maiusculas.
     * @param name Nome
     * @return nome
     */
    public static String toSimpleNameWay(String name){
        if(name == null){
            return "";
        }else {
            String ok = name;
            ok = ok.replace(" > ","-");
            ok = ok.replace(" < ","-");
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
