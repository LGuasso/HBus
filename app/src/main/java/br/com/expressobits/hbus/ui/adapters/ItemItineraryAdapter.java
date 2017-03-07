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
import br.com.expressobits.hbus.dao.BookmarkItineraryDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * Adapter used to display {@link Itinerary}
 * @author Rafael Correa
 * @since 13/08/15.
 */
public class ItemItineraryAdapter extends RecyclerView.Adapter<ItemItineraryAdapter.ItineraryViewHolder> {

    private final List<Itinerary> listItineraries;
    private final LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private final BookmarkItineraryDAO dao;

    public ItemItineraryAdapter(Context context, List<Itinerary> listItineraries) {
        this.listItineraries = listItineraries;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dao = new BookmarkItineraryDAO(context);
    }

    @Override
    public ItineraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = layoutInflater.inflate(R.layout.item_list_simple_itinerary,parent,false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItineraryViewHolder holder, int position) {

        Itinerary itinerary = listItineraries.get(position);
        if(dao.getItinerary(itinerary.getId())!=null){
            holder.imageViewStar.setSelected(true);
           //holder.imageViewStar.setImageResource(context.getResources().getDrawable(R.drawable.ic_star_accent_24dp));

        }else {
            holder.imageViewStar.setSelected(false);
            //holder.imageViewStar.setImageResource(context.getResources().getDrawable(R.drawable.ic_star_outline_accent_24dp));
        }
        holder.textViewCompanyName.setText(FirebaseUtils.getCompany(itinerary.getId()));
        holder.textViewName.setText(itinerary.getName());
    }

    @Override
    public int getItemCount() {
        return listItineraries.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack){
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    class ItineraryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final LinearLayout linearLayout;
        final TextView textViewName;
        final TextView textViewCompanyName;
        final ImageView imageViewStar;

        ItineraryViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutItemList);
            textViewName = (TextView) itemView.findViewById(R.id.text1);
            textViewCompanyName = (TextView) itemView.findViewById(R.id.textViewCompanyName);
            imageViewStar = (ImageView) itemView.findViewById(R.id.icon);
            linearLayout.setOnClickListener(this);
            imageViewStar.setOnClickListener(this);
            if(textViewName.isEnabled()){
                linearLayout.setClickable(true);
            }

        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.icon:
                    if(imageViewStar.isEnabled() & recyclerViewOnClickListenerHack != null){
                        imageViewStar.setSelected(!imageViewStar.isSelected());
                        recyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
                    }
                    break;
                case R.id.linearLayoutItemList:
                    if(recyclerViewOnClickListenerHack != null){
                        recyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
                    }
                    break;
            }
        }
    }
}
