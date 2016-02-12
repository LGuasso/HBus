package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;

/**
 * Adapter usado para exibir {@link Itinerary}
 * @author Rafael Correa
 * @since 13/08/15.
 */
public class ItemItineraryAdapter extends RecyclerView.Adapter<ItemItineraryAdapter.MyViewHolder> {

    private Context context;
    private List<Itinerary> listItineraries;
    private List<Itinerary> favoriteItineraries;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private boolean selectAll;
    int resource;

    public ItemItineraryAdapter(Context context,boolean selectAll,List<Itinerary> listItineraries,List<Itinerary> favoriteItineraries) {
        this.context = context;
        this.listItineraries = listItineraries;
        this.favoriteItineraries = favoriteItineraries;
        this.selectAll = selectAll;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ItemItineraryAdapter(Context context,boolean selectAll,List<Itinerary> listItineraries) {
        this.context = context;
        this.listItineraries = listItineraries;
        this.selectAll = selectAll;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_simple_itinerary,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /**
         *
        if (!selectAll) {
            for (Itinerary itinerary : favoriteItineraries) {
                if (itinerary.getId() == listItineraries.get(position).getId()) {
                    holder.textViewName.setText(listItineraries.get(position).getName());
                    holder.textViewName.setEnabled(false);
                    return;
                }
            }
        }*/

        holder.textViewName.setText(listItineraries.get(position).getName());
        holder.textViewName.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return listItineraries.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack){
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewName;
        public LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            textViewName = (TextView) itemView.findViewById(R.id.textViewItineraryName);
            linearLayout.setOnClickListener(this);
            if(textViewName.isEnabled()){
                linearLayout.setClickable(true);
            }

        }
        @Override
        public void onClick(View v) {
            if(textViewName.isEnabled() & recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
