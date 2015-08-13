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
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.Bus;

/**
 * Created by Rafael on 27/05/2015.
 */
public class ListViewAdapterFavorite extends ArrayAdapter<Itinerary> {

    Context context;
    List<Itinerary> itineraryList;

    public ListViewAdapterFavorite(Context context, int id, List<Itinerary> lista){
        super(context,id,lista);
        this.context = context;
        this.itineraryList = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Itinerary Itinerary = itineraryList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_list_line, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.listView_background_line);
        if(position%2==0){
            //layout.setBackgroundColor(context.getResources().getColor(R.color.orange_ultralight));
        }
        TextView textView = (TextView) view.findViewById(R.id.item_list_textview_line);



        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_nextbuses);

        textView.setText(Itinerary.getName());
        //textViewTipo.setText(linha.getCodigos().toString());
        String texto = new String(" ");
        if(Itinerary.getLines().size()>0){
            ArrayList<Bus> onibuses = Itinerary.getNextBuses();
            int i=0;
            //TODO DESIGN melhorar visual
            for(Bus bus :onibuses){

                if(onibuses!=null){
                    String time = bus.getTime();
                    if(time!=null) {
                        TextView textViewNextbus = new TextView(context);
                        textViewNextbus.setTextAppearance(context,R.style.TextStyle_Medium_Color);
                        textViewNextbus.setText(Itinerary.getSentidos().get(i)+"  -  "+time);
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
