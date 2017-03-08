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
import br.com.expressobits.hbus.ui.adapters.viewholder.HeaderViewHolder;
import br.com.expressobits.hbus.ui.model.Header;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * Adapter used to display {@link Itinerary}
 * @author Rafael Correa
 * @since 13/08/15.
 */
public class ItemItineraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Object> items;
    private final LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private final BookmarkItineraryDAO dao;

    private final int HEADER = 0;
    private final int ITINERARY = 4;

    public ItemItineraryAdapter(Context context, List<Object> items) {
        this.items = items;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dao = new BookmarkItineraryDAO(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case HEADER:
                View viewHeader = inflater.inflate(R.layout.item_header, parent, false);
                viewHolder = new HeaderViewHolder(viewHeader);
                break;
            default:
                View viewItinerary = inflater.inflate(R.layout.item_list_simple_itinerary, parent, false);
                viewHolder = new ItineraryViewHolder(viewItinerary);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Header) {
            return HEADER;
        }else if (items.get(position) instanceof Itinerary) {
            return ITINERARY;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
                configureHeaderViewHolder(headerViewHolder, position);
                break;
            default:
                ItineraryViewHolder itineraryViewHolder = (ItineraryViewHolder) viewHolder;
                configureItineraryViewHolder(itineraryViewHolder, position);
                break;
        }
    }

    private void configureItineraryViewHolder(ItineraryViewHolder itineraryViewHolder, int position) {
        Itinerary itinerary = (Itinerary)items.get(position);
        if(dao.getItinerary(itinerary.getId())!=null){
            itineraryViewHolder.imageViewStar.setSelected(true);
            //holder.imageViewStar.setImageResource(context.getResources().getDrawable(R.drawable.ic_star_accent_24dp));
        }else {
            itineraryViewHolder.imageViewStar.setSelected(false);
            //holder.imageViewStar.setImageResource(context.getResources().getDrawable(R.drawable.ic_star_outline_accent_24dp));
        }
        itineraryViewHolder.textViewCompanyName.setText(FirebaseUtils.getCompany(itinerary.getId()));
        //TODO criar banco de dados e ler dados de lá routes com cores - type de onibus
        if(itinerary.getName().contains("Seletivo")){
            itineraryViewHolder.imageViewItineraryIconType.setBackgroundResource(R.drawable.circle_icon_itinerary_blue_500);
        }else{
            itineraryViewHolder.imageViewItineraryIconType.setBackgroundResource(R.drawable.circle_icon_itinerary_default);
        }
        itineraryViewHolder.textViewName.setText(itinerary.getName());
    }

    private void configureHeaderViewHolder(HeaderViewHolder headerViewHolder, int position) {
        Header header = (Header) items.get(position);
        headerViewHolder.textViewHeader.setText(header.getTextHeader());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack){
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    class ItineraryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final LinearLayout linearLayout;
        final TextView textViewName;
        final TextView textViewCompanyName;
        final ImageView imageViewStar;
        final ImageView imageViewItineraryIconType;

        ItineraryViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutItemList);
            textViewName = (TextView) itemView.findViewById(R.id.textViewItineraryName);
            textViewCompanyName = (TextView) itemView.findViewById(R.id.textViewCompanyName);
            imageViewStar = (ImageView) itemView.findViewById(R.id.icon);
            imageViewItineraryIconType = (ImageView) itemView.findViewById(R.id.imageViewItineraryIconType);
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
