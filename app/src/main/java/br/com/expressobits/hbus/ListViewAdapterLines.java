package br.com.expressobits.hbus;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Rafael on 03/05/2015.
 */
public class ListViewAdapterLines extends ArrayAdapter<String>{


    Context context;
    LayoutInflater layoutInflater;
    Activity activity;
    List<String> lista;


    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public ListViewAdapterLines(Context context,int id,List<String> lista){
        super(context,id,lista);
        this.context = context;
        this.lista = lista;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String string = lista.get(position);
        //View view = br.com.expressobits.fastiw.ui.vendas.ListaVendas.this.getLayoutInflater().inflate(R.layout.listavendas_item,null);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.item_list,null);
        TextView textView = (TextView) view.findViewById(R.id.item_list_textview);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_list_imageview);
        textView.setText(string);
        textView.setTextAppearance(context,R.style.Base_TextAppearance_AppCompat_Large);
        imageView.setImageResource(android.R.drawable.ic_delete);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
