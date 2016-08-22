package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.BusUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * @author Rafael
 * @since 27/05/2015.
 */
public class ItemFavoriteItineraryAdapter extends
        RecyclerView.Adapter<ItemFavoriteItineraryAdapter.HolderFavoriteItinerary>  {

    private Context context;
    public static String PREF_TIME_HOME_SCREEN = "time_home_screen";
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
        String name  = "";
        String companyName = "";
        Itinerary itinerary = itineraryList.get(position);
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MainActivity.DEBUG,false)){
            name += itinerary.getId();
        }else{
            name += itinerary.getName();
            companyName += context.getString(R.string.company_use,FirebaseUtils.getCompany(itinerary.getId()));
        }
        holder.textItineraryName.setText(name);
        holder.textViewCompanyName.setText(companyName);
        if(itinerary.getWays().size()>0 & PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ItemFavoriteItineraryAdapter.PREF_TIME_HOME_SCREEN,true)){
            getBusList(holder,itinerary).toString();
        }

    }

    /**
     * TODO identificar forma possivel de diminuir a lentidão de processo deste método
     *
     * Método que identifica quais serão os próximos ônibus para
     *
     * @param holder
     * @param itinerary
     * @return
     */
    private HashMap<String,Bus> getBusList(final HolderFavoriteItinerary holder, final Itinerary itinerary){

        final HashMap<String,Bus> next = new HashMap<>();
        if(itinerary.getWays()!=null) {
            for (int j = 0; j < itinerary.getWays().size(); j++) {
                final String way = itinerary.getWays().get(j);
                final List<Bus> buses = new ArrayList<>();
                final String typeday = HoursUtils.getTypedayinCalendar(Calendar.getInstance()).toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference busTable = database.getReference(FirebaseUtils.BUS_TABLE);
                DatabaseReference countryRef = busTable.child(FirebaseUtils.getCountry(itinerary.getId()));
                DatabaseReference cityRef = countryRef.child(FirebaseUtils.getCityName(itinerary.getId()));
                DatabaseReference companyRef = cityRef.child(FirebaseUtils.getCompany(itinerary.getId()));
                DatabaseReference itineraryRef = companyRef.child(itinerary.getName());
                DatabaseReference wayRef = itineraryRef.child(way);
                DatabaseReference typedayRef = wayRef.child(typeday);
                typedayRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Bus bus = dataSnapshot.getValue(Bus.class);
                        bus.setId(FirebaseUtils.getIdBus(FirebaseUtils.getCountry(itinerary.getId()),
                                FirebaseUtils.getCityName(itinerary.getId()),
                                FirebaseUtils.getCompany(itinerary.getId()),
                                itinerary.getName(),
                                way,
                                typeday,
                                String.valueOf(bus.getTime())));
                        buses.add(bus);
                        next.put(way,BusUtils.getNextBusforList(buses));
                        setLastUpdate(holder,bus.getTime());
                        updateFieldNextBus(holder,itinerary,next);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }
        return next;
    }

    private void setLastUpdate(HolderFavoriteItinerary holder,long milis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milis);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy");
        String format = sdf.format(calendar.getTime());
        holder.textViewUpdated.setText(context.getString(R.string.updated_date,format));

    }

    private void updateFieldNextBus(HolderFavoriteItinerary holder,Itinerary itinerary,HashMap<String,Bus> next){

        holder.linearLayoutHours.removeAllViews();
        for(int i=0;i<next.size();i++) {
            String way = itinerary.getWays().get(i);
            View view = layoutInflater.inflate(R.layout.item_next_bus, holder.linearLayoutHours, false);
            TextView textViewHour = (TextView) view.findViewById(R.id.textViewHourforNextBus);
            TextView textViewWay = (TextView) view.findViewById(R.id.textViewWayforNextBus);
            TextView textViewCode = (TextView) view.findViewById(R.id.textViewCodeforNextBus);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String time = sdf.format(next.get(way).getTime());
            String code = next.get(way).getCode();
            textViewWay.setText(way);
            textViewHour.setText(time);
            textViewCode.setText(code);
            holder.linearLayoutHours.addView(view, i);
        }
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
        public TextView textViewCompanyName;
        public TextView textViewUpdated;
        public Button buttonRemove;
        public Button buttonLookHours;
        public LinearLayout linearLayoutInfo;
        public LinearLayout linearLayoutHours;

        public HolderFavoriteItinerary(View itemView) {
            super(itemView);

            textItineraryName = (TextView) itemView.findViewById(R.id.textViewItineraryName);
            textViewCompanyName = (TextView) itemView.findViewById(R.id.textViewCompanyName);
            textViewUpdated = (TextView) itemView.findViewById(R.id.textViewLastUpdate);
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
