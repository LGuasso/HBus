package br.com.expressobits.hbus.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.ScheduleDAO;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.adapters.viewholder.HotTipViewHolder;
import br.com.expressobits.hbus.ui.adapters.viewholder.HeaderViewHolder;
import br.com.expressobits.hbus.ui.adapters.viewholder.NewsViewHolder;
import br.com.expressobits.hbus.ui.model.HotTip;
import br.com.expressobits.hbus.ui.model.Header;
import br.com.expressobits.hbus.ui.news.NewsDetailsActivity;
import br.com.expressobits.hbus.utils.BusUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael
 * @since 27/05/2015.
 */
public class ItemHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewOnClickListenerHack {

    private final Context context;
    private static final String PREF_TIME_HOME_SCREEN = "time_home_screen";
    private final List<Object> items;
    private final LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private final HashMap<String,HashMap<String,Code>> codes = new HashMap<>();

    private final int HEADER = 0;
    private final int HOT_TIP = 1;
    private final int NEWS = 2;
    private final int BOOKMARKEDITINERARY = 3;

    public ItemHomeAdapter(Context context, List<Object> list){
        this.context = context;
        this.items = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType){
            case HEADER:
                View viewHeader = inflater.inflate(R.layout.item_header, viewGroup, false);
                viewHolder = new HeaderViewHolder(viewHeader);
                break;
            case HOT_TIP:
                View viewGetStarted = inflater.inflate(R.layout.item_home_get_started_empty_state, viewGroup, false);
                viewHolder = new HotTipViewHolder(viewGetStarted);
                break;
            case NEWS:
                View viewNews = inflater.inflate(R.layout.item_news, viewGroup, false);
                viewHolder = new NewsViewHolder(viewNews);
                break;
            case BOOKMARKEDITINERARY:
                View viewItinerary = inflater.inflate(R.layout.item_bookmarked_itinerary, viewGroup, false);
                viewHolder = new BookmarkedItineraryViewHolder(viewItinerary);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                viewHolder = new BookmarkedItineraryViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Header) {
            return HEADER;
        }else if (items.get(position) instanceof HotTip) {
            return HOT_TIP;
        }else if (items.get(position) instanceof News) {
            return NEWS;
        }else if (items.get(position) instanceof Itinerary) {
            return BOOKMARKEDITINERARY;
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
            case HOT_TIP:
                HotTipViewHolder hotTipViewHolder = (HotTipViewHolder) viewHolder;
                configureHotTipViewHolder(hotTipViewHolder, position);
                break;
            case NEWS:
                NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;
                configureNewsViewHolder(newsViewHolder, position);
                break;
            case BOOKMARKEDITINERARY:
                BookmarkedItineraryViewHolder bookmarkedItineraryViewHolder = (BookmarkedItineraryViewHolder) viewHolder;
                configureBookmarkedItineraryViewHolder(bookmarkedItineraryViewHolder, position);
                break;
            default:
                BookmarkedItineraryViewHolder defaultViewHolder = (BookmarkedItineraryViewHolder) viewHolder;
                configureBookmarkedItineraryViewHolder(defaultViewHolder, position);
                break;
        }


    }

    private void configureHeaderViewHolder(HeaderViewHolder headerViewHolder, int position) {
        Header header = (Header) items.get(position);
        headerViewHolder.textViewHeader.setText(header.getTextHeader());
    }

    private void configureHotTipViewHolder(HotTipViewHolder hotTipViewHolder, int position) {
        HotTip hotTip = (HotTip)items.get(position);
        hotTipViewHolder.textViewTitle.setText(hotTip.getTitle());
        hotTipViewHolder.textViewMessage.setText(hotTip.getMessage());
        hotTipViewHolder.imageView.setImageResource(hotTip.getImageResource());
        hotTipViewHolder.button1.setText(hotTip.getButtonText());
        if(hotTip.getButton2Text()==null){
            hotTipViewHolder.button2.setEnabled(false);
            hotTipViewHolder.button2.setVisibility(View.INVISIBLE);
        }else{
            hotTipViewHolder.button2.setEnabled(true);
            hotTipViewHolder.button2.setVisibility(View.VISIBLE);
            hotTipViewHolder.button2.setText(hotTip.getButton2Text());
        }
        hotTipViewHolder.setRecyclerViewOnClickListenerHack(this);
    }

    private void configureNewsViewHolder(NewsViewHolder newsViewHolder, int position) {
        News news = (News)items.get(position);
        NewsViewHolder.bindNews(layoutInflater,context,newsViewHolder, news);
        newsViewHolder.setRecyclerViewOnClickListenerHack(this);
    }

    private void configureBookmarkedItineraryViewHolder(BookmarkedItineraryViewHolder bookmarkedItineraryViewHolder, int position) {
        String name  = "";
        String companyName = "";
        Itinerary itinerary = (Itinerary)items.get(position);
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(MainActivity.DEBUG,false)){
            name += itinerary.getId();
        }else{
            name += itinerary.getName();
            companyName += context.getString(R.string.company_use,FirebaseUtils.getCompany(itinerary.getId()));
        }
        bookmarkedItineraryViewHolder.textItineraryName.setText(name);
        bookmarkedItineraryViewHolder.textViewCompanyName.setText(companyName);
        if(itinerary.getWays().size()>0 & PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ItemHomeAdapter.PREF_TIME_HOME_SCREEN,true)){
            getBusList(bookmarkedItineraryViewHolder,itinerary);
        }else{
            bookmarkedItineraryViewHolder.textViewUpdated.setText("");
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
    private void getBusList(final BookmarkedItineraryViewHolder holder, final Itinerary itinerary){

        final HashMap<String,Bus> next = new HashMap<>();
        String country = FirebaseUtils.getCountry(itinerary.getId());
        String city = FirebaseUtils.getCityName(itinerary.getId());
        String company = FirebaseUtils.getCompany(itinerary.getId());
        if(itinerary.getWays()!=null) {
            for (int j = 0; j < itinerary.getWays().size(); j++) {
                final String way = itinerary.getWays().get(j);
                List<Bus> buses = new ArrayList<>();
                final String typeday = TimeUtils.getTypedayinCalendar(Calendar.getInstance()).toString();
                ScheduleDAO dao = new ScheduleDAO(context,country,city);
                buses = dao.getBuses(company,itinerary.getName(),way,typeday);
                dao.close();

                if(buses.size()>0){
                    next.put(way, BusUtils.getNextBusforList(buses));
                    setLastUpdate(holder, buses.get(0).getTime());
                    updateFieldNextBus(holder, itinerary, next);
                }
            }
        }
    }

    private void setLastUpdate(BookmarkedItineraryViewHolder holder, long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy", Locale.getDefault());
        String format = sdf.format(calendar.getTime());
        holder.textViewUpdated.setText(context.getString(R.string.updated_date,format));

    }

    private void updateFieldNextBus(BookmarkedItineraryViewHolder holder, Itinerary itinerary, HashMap<String,Bus> next){

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
        return items.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
        this.recyclerViewOnClickListenerHack = recyclerViewOnClickListenerHack;
    }

    private void loadCode(TextView textView,String codeName,String company,String country,String cityName){
        if(!codes.containsKey(codeName)){
            ScheduleDAO dao = new ScheduleDAO(context,country,cityName);
            Code code = dao.getCode(company,codeName);
            if(code!=null){
                addCode(textView,company,codeName,code);
            }
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

    @Override
    public void onClickListener(View view, int position) {
        if(items.get(position) instanceof News){
            News news = (News)items.get(position);
            Intent intent = new Intent(context, NewsDetailsActivity.class);
            intent.putExtra(NewsDetailsActivity.ARGS_NEWS_ID,news.getId());
            context.startActivity(intent);
        }
        if(items.get(position) instanceof HotTip){
            recyclerViewOnClickListenerHack.onClickListener(view, position);
        }
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

    private class BookmarkedItineraryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        private final TextView textItineraryName;
        private final TextView textViewCompanyName;
        private final TextView textViewUpdated;
        private final Button buttonRemove;
        private final Button buttonLookHours;
        private final LinearLayout linearLayoutHours;

        private BookmarkedItineraryViewHolder(View itemView) {
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

}
