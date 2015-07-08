package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.modelo.Itinerario;
import br.com.expressobits.hbus.modelo.Bus;

/**
 * Created by Rafael on 27/05/2015.
 */
public class ListViewAdapterFavorite extends ArrayAdapter<Itinerario> {

    Context context;
    LayoutInflater layoutInflater;
    List<Itinerario> itinerarioList;

    public ListViewAdapterFavorite(Context context, int id, List<Itinerario> lista){
        super(context,id,lista);
        this.context = context;
        //this.lista = lista;
        this.itinerarioList = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Itinerario itinerario = itinerarioList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_list_line, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.listView_background_line);
        if(position%2==0){
            //layout.setBackgroundColor(context.getResources().getColor(R.color.orange_ultralight));
        }
        TextView textView = (TextView) view.findViewById(R.id.item_list_textview_line);



        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_nextbuses);

        textView.setText(itinerario.getNome());
        //textViewTipo.setText(linha.getCodigos().toString());
        String texto = new String(" ");
        if(itinerario.getLinhas().size()>0){
            ArrayList<Bus> onibuses = itinerario.getNextBuses();
            int i=0;
            //TODO DESIGN melhorar visual
            for(Bus bus :onibuses){

                if(onibuses!=null){
                    String time = bus.getTime();
                    if(time!=null) {
                        TextView textViewNextbus = new TextView(context);
                        textViewNextbus.setTextAppearance(context,R.style.TextStyle_Medium_Color);
                        textViewNextbus.setText(itinerario.getSentidos().get(i)+"  -  "+time);
                        linearLayout.addView(textViewNextbus);
                    }else{
                    }
                }else{
                }
                i++;
            }
        }
        return view;
    }
}
