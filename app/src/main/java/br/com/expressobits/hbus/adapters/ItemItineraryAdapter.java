package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Itinerary;

/**
 * Created by rafael on 13/08/15.
 */
public class ItemItineraryAdapter extends ArrayAdapter<Itinerary>{

    Context context;
    List<Itinerary> list;

    public ItemItineraryAdapter(Context context, int resource,List<Itinerary> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Itinerary itinerary = list.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_list_line_add, null);
        TextView textViewItineraryName = (TextView)view.findViewById(R.id.textViewItineraryName);
        textViewItineraryName.setText(itinerary.getName());

        return  view;
    }
}
