package br.com.expressobits.hbus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import br.com.expressobits.hbus.modelo.Onibus;

/**
 * Created by Rafael on 03/05/2015.
 */
public class ListViewAdapterLines extends ArrayAdapter<Onibus>{


    Context context;
    LayoutInflater layoutInflater;
    Activity activity;
    List<Onibus> onibusList;


    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public ListViewAdapterLines(Context context,int id,List<Onibus> lista){
        super(context,id,lista);
        this.context = context;
        //this.lista = lista;
        this.onibusList = lista;

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Onibus onibus = onibusList.get(position);
        //View view = br.com.expressobits.fastiw.ui.vendas.ListaVendas.this.getLayoutInflater().inflate(R.layout.listavendas_item,null);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.item_list,null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.listView_background);
        if(position%2==0){
            layout.setBackgroundColor(context.getResources().getColor(R.color.orange_ultralight));
        }
        TextView textView = (TextView) view.findViewById(R.id.item_list_textview);
        TextView textViewUltimo = (TextView) view.findViewById(R.id.item_list_textview_ultimo);
        TextView textViewCodigo = (TextView) view.findViewById(R.id.item_list_textview_codigo);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_list_imageview);
        textView.setText(onibus.getHorario());
        textViewCodigo.setText(context.getString(R.string.code_of_bus) + " " + onibus.getCodigo());
        textView.setTextAppearance(context, R.style.Base_TextAppearance_AppCompat_Large);


        int hora = new GregorianCalendar().get(Calendar.HOUR_OF_DAY);
        int minuto = new GregorianCalendar().get(Calendar.MINUTE);


        if(onibus.getHora()-hora==0){
            if(onibus.getMinuto()>minuto){
                textViewUltimo.setText("Falta "+(onibus.getMinuto()-minuto)+" minuto(s)");
                textViewUltimo.setTextColor(context.getResources().getColor(R.color.red));
            }
        }else{
            if(onibus.getHora()>hora){
                textViewUltimo.setText("Falta "+(onibus.getHora()-hora)+" hora(s)");
                textViewUltimo.setTextColor(context.getResources().getColor(R.color.green));
            }
        }


        imageView.setImageResource(android.R.drawable.ic_delete);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
