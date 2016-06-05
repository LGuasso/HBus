package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.FavoriteDAO;
import br.com.expressobits.hbus.model.Itinerary;
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
    FavoriteDAO dao;

    public ItemItineraryAdapter(Context context,boolean selectAll,List<Itinerary> listItineraries,List<Itinerary> favoriteItineraries) {
        this.context = context;
        this.listItineraries = listItineraries;
        this.favoriteItineraries = favoriteItineraries;
        this.selectAll = selectAll;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dao = new FavoriteDAO(context);
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

        if(dao.getItinerary(listItineraries.get(position).getId())!=null){
            holder.imageViewStar.setSelected(true);
           // holder.imageViewStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_accent_24dp));

        }else {
            holder.imageViewStar.setSelected(false);
            //holder.imageViewStar.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_outline_accent_24dp));
        }

        holder.textViewName.setText(listItineraries.get(position).getName());
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
        public ImageView imageViewStar;


        public MyViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutItemList);
            textViewName = (TextView) itemView.findViewById(R.id.textViewItineraryName);
            imageViewStar = (ImageView) itemView.findViewById(R.id.imageViewStar);
            linearLayout.setOnClickListener(this);
            imageViewStar.setOnClickListener(this);
            if(textViewName.isEnabled()){
                linearLayout.setClickable(true);
            }

        }
        @Override
        public void onClick(View v) {


            switch (v.getId()){
                case R.id.imageViewStar:
                    if(imageViewStar.isEnabled() & recyclerViewOnClickListenerHack != null){
                        imageViewStar.setSelected(!imageViewStar.isSelected());
                        recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
                    }
                    break;
                case R.id.linearLayoutItemList:
                    if(recyclerViewOnClickListenerHack != null){
                        recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
                    }
                    break;
            }


        }
    }
}
