package br.com.expressobits.hbus.utils;

import java.util.List;

import br.com.expressobits.hbus.model.TypeDay;

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

    public static TypeDay getTypeDAyString(String typeDay){
        if(typeDay.equals(TypeDay.USEFUL.toString())){
            return TypeDay.USEFUL;
        }else if(typeDay.equals(TypeDay.SATURDAY.toString())){
            return TypeDay.SATURDAY;
        }else if(typeDay.equals(TypeDay.SUNDAY.toString())){
            return TypeDay.SUNDAY;
        }
        return TypeDay.SUNDAY;

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
            ok = ok.replace(" ","-");
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

    public static String getTypeDayInt(int typeDay){
        if(typeDay==0){
            return TypeDay.USEFUL.toString();
        }else if(typeDay==1){
            return TypeDay.SATURDAY.toString();
        }else if(typeDay==2){
            return TypeDay.SUNDAY.toString();
        }
        return "";
    }

    public static int getTypeDayInt(String typeDay){
        if(typeDay.equals(TypeDay.USEFUL.toString())){
            return 0;
        }else if(typeDay.equals(TypeDay.SATURDAY.toString())){
            return 1;
        }else if(typeDay.equals(TypeDay.SUNDAY.toString())){
            return 2;
        }
        return 0;
    }
}
