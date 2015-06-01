package br.com.expressobits.hbus.file;

import android.content.Context;
import android.renderscript.Element;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.expressobits.hbus.modelo.Linha;
import br.com.expressobits.hbus.modelo.Onibus;
import br.com.expressobits.hbus.modelo.TipoDeDia;

/**
 * Created by Rafael on 20/05/2015.
 */
public class LinhaFile {

    ArrayList<Onibus> onibusList = new ArrayList<Onibus>();
    ArrayList<String> texto= new ArrayList<String>();
    ArrayList<Linha> linhasList = new ArrayList<Linha>();

    public LinhaFile(String nome,String sentido,TipoDeDia dias,Context context){
        this((nome+"_"+sentido+"_"+dias),context);
    }

    public LinhaFile(String nome,Context context){

        ManageFile file = new ManageFile(context);
        String nameFile = nome+".dat";
        Toast.makeText(context,nameFile,Toast.LENGTH_LONG).show();
        String fileString = file.ReadFile(nameFile);
        String[] fileStringarray=fileString.split("\n");
        texto = new ArrayList<String>(Arrays.asList(fileStringarray));
    }


    public List<Onibus> getOnibusList() {
        if(!texto.isEmpty() && !texto.get(0).equals("ERRO")) {
            for (String txt : texto) {
                Onibus onibus = new Onibus();
                onibus.setHorario(txt.split(" - ")[0]);
                onibus.setCodigo(txt.split(" - ")[1]);
                onibusList.add(onibus);
            }
        }

        return onibusList;
    }

    public List<Linha> getLinhasList() {
        if(!texto.isEmpty() && !texto.get(0).equals("ERRO")) {
            for (String txt : texto) {
                Linha linha = new Linha();
                linha.setNome(txt.split(" - ")[0]);
                linha.setTipos(new ArrayList<String>(Arrays.asList(txt.split(" - ")[1].split(":"))));
                linhasList.add(linha);
            }
        }

        return linhasList;
    }
}
