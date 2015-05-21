package br.com.expressobits.hbus.file;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.expressobits.hbus.modelo.TipoDeDia;

/**
 * Created by Rafael on 20/05/2015.
 */
public class LinhaFile {

    ArrayList<String> horarios=new ArrayList<String>();


    public LinhaFile(String nome,boolean sentido,TipoDeDia dias,Context context){
        ManageFile file = new ManageFile(context);
        String nameFile = nome+"_"+sentido+"_"+dias;
        Toast.makeText(context,nameFile,Toast.LENGTH_LONG).show();
        String fileString = file.ReadFile(nameFile);
        String[] fileStringarray=fileString.split("\n");
        horarios = new ArrayList<String>(Arrays.asList(fileStringarray));
    }

    public List<String> getHorarios() {
        return horarios;
    }
}
