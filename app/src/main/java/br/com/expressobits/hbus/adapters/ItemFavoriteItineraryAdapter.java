package br.com.expressobits.hbus.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.ui.MainActivity;

/**
 * @author Rafael
 * @since 27/05/2015.
 */
public class ItemFavoriteItineraryAdapter extends RecyclerView.Adapter<ItemFavoriteItineraryAdapter.HolderFavoriteItinerary>  {

    private Context context;
    private List<Itinerary> itineraryList;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ItemFavoriteItineraryAdapter(Context context, List<Itinerary> lista){
        this.context = context;
        this.itineraryList = lista;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public HolderFavoriteItinerary onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.item_favorite_itinerary,viewGroup,false);
        return new HolderFavoriteItinerary(view,itineraryList);
    }



    @Override
    public void onBindViewHolder(HolderFavoriteItinerary holder, int position) {
        BusDAO dao = new BusDAO(context);
        String name  = "";
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MainActivity.DEBUG,false)){
            name += itineraryList.get(position).getId()+" - "+itineraryList.get(position).getName();
        }else{
            name += itineraryList.get(position).getName();
        }
        holder.textItineraryName.setText(name);
        holder.linearLayout  = (TextView) holder.itemView.findViewById(R.id.linear_layout_nextbuses);
        if(itineraryList.get(position).getWays().size()>0){
            ArrayList<Bus> onibuses = new ArrayList<>(dao.getNextBuses(itineraryList.get(position)));
            LinearLayout linearLayout = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(params);
            String texto = "";
            for(int i=0;i<onibuses.size();i++){
                Bus bus = onibuses.get(i);

                if(onibuses!=null){
                    String time = bus.getTime();
                    if(time!=null) {

                        texto+=itineraryList.get(position).getWays().get(i)+"  -  "+time;
                        if(i!=onibuses.size()-1){
                            texto+="\n";
                        }

                    }else{
                    }
                }else{
                }
            }
            holder.linearLayout.setText(texto);
        }
        dao.close();

    }


    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    public class HolderFavoriteItinerary extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        public TextView textItineraryName;
        public TextView linearLayout;
        public Button buttonRemove;
        public Button buttonLookHours;
        public LinearLayout linearLayoutInfo;

        public HolderFavoriteItinerary(View itemView,List<Itinerary> list) {
            super(itemView);

            textItineraryName = (TextView) itemView.findViewById(R.id.textViewItineraryName);
            linearLayout  = (TextView) itemView.findViewById(R.id.linear_layout_nextbuses);
            linearLayoutInfo = (LinearLayout) itemView.findViewById(R.id.linearlayout_background_info);
            buttonRemove = (Button) itemView.findViewById(R.id.buttonRemove);
            buttonLookHours = (Button) itemView.findViewById(R.id.buttonLookTime);

            buttonLookHours.setOnClickListener(this);
            buttonRemove.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                return recyclerViewOnClickListenerHack.onLongClickListener(v, getPosition());
            }
            return false;
        }
    }
}
