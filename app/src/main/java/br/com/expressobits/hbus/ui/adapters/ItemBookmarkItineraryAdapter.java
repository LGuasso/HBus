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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.utils.BusUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael
 * @since 27/05/2015.
 */
public class ItemBookmarkItineraryAdapter extends
        RecyclerView.Adapter<ItemBookmarkItineraryAdapter.HolderFavoriteItinerary>  {

    private final Context context;
    private static final String PREF_TIME_HOME_SCREEN = "time_home_screen";
    private final List<Itinerary> itineraryList;
    private final LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private final HashMap<String,HashMap<String,Code>> codes = new HashMap<>();

    public ItemBookmarkItineraryAdapter(Context context, List<Itinerary> list){
        this.context = context;
        this.itineraryList = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public HolderFavoriteItinerary onCreateViewHolder(ViewGroup viewGroup, int j) {
        View viewP;
        viewP = layoutInflater.inflate(R.layout.item_favorite_itinerary,viewGroup,false);
        return new HolderFavoriteItinerary(viewP);
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
        if(itinerary.getWays().size()>0 & PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ItemBookmarkItineraryAdapter.PREF_TIME_HOME_SCREEN,true)){
            getBusList(holder,itinerary);
        }

    }

    /**
     * TODO identify possible way to decrease the process slowness of this method
     *
     * Method that identifies the next buses
     *
     * @param holder Holder view
     * @param itinerary Itinerary
     */
    private void getBusList(final HolderFavoriteItinerary holder, final Itinerary itinerary){

        final HashMap<String,Bus> next = new HashMap<>();
        String country = FirebaseUtils.getCountry(itinerary.getId());
        String city = FirebaseUtils.getCityName(itinerary.getId());
        String company = FirebaseUtils.getCompany(itinerary.getId());
        if(itinerary.getWays()!=null) {
            for (int j = 0; j < itinerary.getWays().size(); j++) {
                final String way = itinerary.getWays().get(j);
                final List<Bus> buses = new ArrayList<>();
                final String typeday = TimeUtils.getTypedayinCalendar(Calendar.getInstance()).toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference busTable = database.getReference(FirebaseUtils.BUS_TABLE);
                DatabaseReference countryRef = busTable.child(country);
                DatabaseReference cityRef = countryRef.child(city);
                DatabaseReference companyRef = cityRef.child(company);
                DatabaseReference itineraryRef = companyRef.child(itinerary.getName());
                DatabaseReference wayRef = itineraryRef.child(way);
                DatabaseReference typedayRef = wayRef.child(typeday);
                typedayRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshotBus : dataSnapshot.getChildren()) {
                            Bus bus = dataSnapshotBus.getValue(Bus.class);
                            bus.setId(FirebaseUtils.getIdBus(FirebaseUtils.getCountry(itinerary.getId()),
                                    FirebaseUtils.getCityName(itinerary.getId()),
                                    FirebaseUtils.getCompany(itinerary.getId()),
                                    itinerary.getName(),
                                    way,
                                    typeday,
                                    String.valueOf(bus.getTime())));
                            buses.add(bus);
                            next.put(way, BusUtils.getNextBusforList(buses));
                            setLastUpdate(holder, bus.getTime());
                            updateFieldNextBus(holder, itinerary, next);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
        }
    }

    private void setLastUpdate(HolderFavoriteItinerary holder,long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy", Locale.getDefault());
        String format = sdf.format(calendar.getTime());
        holder.textViewUpdated.setText(context.getString(R.string.updated_date,format));

    }

    private void updateFieldNextBus(HolderFavoriteItinerary holder,Itinerary itinerary,HashMap<String,Bus> next){

        holder.linearLayoutHours.removeAllViews();
        for(int i=0;i<next.size();i++) {
            String way = itinerary.getWays().get(i);
            View view;
            view = layoutInflater.inflate(R.layout.item_next_bus, holder.linearLayoutHours, false);
            TextView textViewHour = (TextView) view.findViewById(R.id.textViewHourforNextBus);
            TextView textViewWay = (TextView) view.findViewById(R.id.textViewWayforNextBus);
            TextView textViewCode = (TextView) view.findViewById(R.id.textViewCodeforNextBus);

            try{
                String time = TimeUtils.getFormatTime(next.get(way).getTime());
                String code = next.get(way).getCode();
                textViewWay.setText(way);
                textViewHour.setText(time);
                code=code.replace(" ","");
                textViewCode.setText(code);
                if(code.length()<= Code.CODE_LENGTH_TO_DESCRIPTION){
                    loadCode(textViewCode,code,FirebaseUtils.getCompany(itinerary.getId()),
                            FirebaseUtils.getCountry(itinerary.getId()),FirebaseUtils.getCityName(itinerary.getId()));
                }

                textViewCode.setSelected(true);
                holder.linearLayoutHours.addView(view, i);
            }catch (NullPointerException ex){
                System.err.println("NullPointerException in load codes context!");
                ex.printStackTrace();
            }


        }
    }


    @Override
    public int getItemCount() {
        return itineraryList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    class HolderFavoriteItinerary extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        private final TextView textItineraryName;
        private final TextView textViewCompanyName;
        private final TextView textViewUpdated;
        private final Button buttonRemove;
        private final Button buttonLookHours;
        private final LinearLayout linearLayoutHours;

        private HolderFavoriteItinerary(View itemView) {
            super(itemView);

            textItineraryName = (TextView) itemView.findViewById(R.id.textViewItineraryName);
            textViewCompanyName = (TextView) itemView.findViewById(R.id.textViewCompanyName);
            textViewUpdated = (TextView) itemView.findViewById(R.id.textViewLastUpdate);
            linearLayoutHours = (LinearLayout) itemView.findViewById(R.id.linearLayoutHours);
            buttonRemove = (Button) itemView.findViewById(R.id.buttonRemove);
            buttonLookHours = (Button) itemView.findViewById(R.id.buttonLookTime);
            buttonLookHours.setOnClickListener(this);
            buttonRemove.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.onClickListener(v,this.getAdapterPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            return recyclerViewOnClickListenerHack != null && recyclerViewOnClickListenerHack.onLongClickListener(v,getAdapterPosition());
        }
    }

    private void loadCode(TextView textView,String codeName,String company,String country,String cityName){
        if(!codes.containsKey(codeName)){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference busTable = database.getReference(FirebaseUtils.CODE_TABLE);
            DatabaseReference countryRef = busTable.child(country);
            DatabaseReference cityRef = countryRef.child(cityName);
            DatabaseReference companyRef = cityRef.child(company);
            DatabaseReference codeRef = companyRef.child(codeName);
            codeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Code code = dataSnapshot.getValue(Code.class);
                    if(code!=null){
                        addCode(textView,company,codeName,code);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            addCode(textView,company,codeName);
        }

    }


    private void addCode(TextView textView,String company,String codeName,Code code){
        if(!codes.containsKey(company)){
            codes.put(company,new HashMap<>());
        }
        codes.get(company).put(codeName,code);
        updateCodeViews(textView,code);
    }

    private void addCode(TextView textView,String company,String codeName){
        updateCodeViews(textView,codes.get(company).get(codeName));
    }

    private void updateCodeViews(TextView textView,Code code){
        if(code.getDescrition()!=null){
            textView.setText(code.getDescrition());
        }
        textView.setSelected(true);

    }


}
