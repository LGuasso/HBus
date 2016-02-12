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
import br.com.expressobits.hbus.backend.itineraryApi.model.Itinerary;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;

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
    public HolderFavoriteItinerary onCreateViewHolder(ViewGroup viewGroup, int j) {
        View viewP = layoutInflater.inflate(R.layout.item_favorite_itinerary,viewGroup,false);
        HolderFavoriteItinerary holder = new HolderFavoriteItinerary(viewP);

        return holder;
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
        if(itineraryList.get(position).getWays().size()>0){
            ArrayList<Bus> onibuses = new ArrayList<>(dao.getNextBus(PreferenceManager.getDefaultSharedPreferences(context).getString(SelectCityActivity.TAG,null),itineraryList.get(position).getId()));

            holder.linearLayoutHours.removeAllViews();
            for(int i=0;i<onibuses.size();i++){
                View view = layoutInflater.inflate(R.layout.item_next_bus,holder.linearLayoutHours,false);
                TextView textViewHour = (TextView)view.findViewById(R.id.textViewHourforNextBus);
                TextView textViewWay = (TextView)view.findViewById(R.id.textViewWayforNextBus);


                Bus bus = onibuses.get(i);

                String time = bus.getTime();
                if(time!=null) {

                    //texto+=itineraryList.get(position).getWays().get(i)+"  -  "+time;
                    textViewWay.setText(itineraryList.get(position).getWays().get(i));
                    textViewHour.setText(time);

                }
                holder.linearLayoutHours.addView(view, i);
            }

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
        //public TextView textViewItineraryHours;
        public Button buttonRemove;
        public Button buttonLookHours;
        public LinearLayout linearLayoutInfo;
        public LinearLayout linearLayoutHours;

        public HolderFavoriteItinerary(View itemView) {
            super(itemView);

            textItineraryName = (TextView) itemView.findViewById(R.id.textViewItineraryName);
            //textViewItineraryHours = (TextView) itemView.findViewById(R.id.linear_layout_nextbuses);
            linearLayoutInfo = (LinearLayout) itemView.findViewById(R.id.linearlayout_background_info);
            linearLayoutHours = (LinearLayout) itemView.findViewById(R.id.linearLayoutHours);
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
            return recyclerViewOnClickListenerHack != null && recyclerViewOnClickListenerHack.onLongClickListener(v, getPosition());
        }
    }
}
