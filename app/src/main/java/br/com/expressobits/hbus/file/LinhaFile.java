package br.com.expressobits.hbus.file;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.expressobits.hbus.modelo.Onibus;
import br.com.expressobits.hbus.modelo.TipoDeDia;

/**
 * Created by Rafael on 20/05/2015.
 */
public class LinhaFile {

    ArrayList<Onibus> horarios=new ArrayList<Onibus>();

    ArrayList<String> texto= new ArrayList<String>();


    public LinhaFile(String nome,String sentido,TipoDeDia dias,Context context){

                ManageFile file = new ManageFile(context);
                String nameFile = nome+"_"+sentido+"_"+dias+".dat";
                Toast.makeText(context,nameFile,Toast.LENGTH_LONG).show();
                String fileString = file.ReadFile(nameFile);
                String[] fileStringarray=fileString.split("\n");
                texto = new ArrayList<String>(Arrays.asList(fileStringarray));


    }

    public List<Onibus> getHorarios() {

        for(String txt : texto){
            Onibus onibus = new Onibus();
            onibus.setHorario(txt.split(" - ")[0]);
            onibus.setCodigo(txt.split(" - ")[1]);
            horarios.add(onibus);
        }


        return horarios;
    }
}
