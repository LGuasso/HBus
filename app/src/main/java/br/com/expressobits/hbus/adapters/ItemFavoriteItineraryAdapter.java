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
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.Bus;

/**
 * Created by Rafael on 27/05/2015.
 */
public class ItemFavoriteItineraryAdapter extends ArrayAdapter<Itinerary> {

    Context context;
    List<Itinerary> itineraryList;

    public ItemFavoriteItineraryAdapter(Context context, int id, List<Itinerary> lista){
        super(context,id,lista);
        this.context = context;
        this.itineraryList = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BusDAO dao = new BusDAO(context);
        Itinerary itinerary = dao.getItinerary(itineraryList.get(position).getName());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_favorite_itinerary, null);
        TextView textView = (TextView) view.findViewById(R.id.textViewItineraryName);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout_nextbuses);
        textView.setText(itinerary.getName());
        if(itinerary.getSentidos().size()>0){
            ArrayList<Bus> onibuses = new ArrayList<>(dao.getNextBuses(itinerary));
            int i=0;
            //TODO DESIGN melhorar visual imageview circle
            for(Bus bus :onibuses){

                if(onibuses!=null){
                    String time = bus.getTime();
                    if(time!=null) {
                        TextView textViewNextbus = new TextView(context);
                        textViewNextbus.setTextAppearance(context,R.style.TextStyle_Medium_Color);
                        textViewNextbus.setText(itinerary.getSentidos().get(i)+"  -  "+time);
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
