package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.ScheduleDAO;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.ui.adapters.ItemBusAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment{

    private RecyclerView recyclerView;
    private ViewGroup layoutEmptyState;
    private ProgressBar progressBar;
    private List<Bus> listBus;
    //private Context context;
    private static final String TAG = "HoursFragment";
    public static final String ARGS_COUNTRY = "country";
    public static final String ARGS_CITY = "city";
    public static final String ARGS_COMPANY = "company";
    public static final String ARGS_ITINERARY = "itinerary";
    public static final String ARGS_WAY = "Way";
    public static final String ARGS_TYPEDAY = "typeday";
    public static final String ARGS_CODES = "codesNoSelected";
    private String country;
    private String city;
    private String company;
    private String itinerary;
    private String way;
    private String typeday;
    private ArrayList<String> codesNoSelected;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        //this.context = inflater.getContext();
        initRecyclerView(view);
        initEmptyStateViews(view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listBus = new ArrayList<>();
        //loadParams(getArguments());
        Log.d(TAG,typeday+" onCreateView");
        return view;
    }

    public void loadParams(Bundle arguments) {
        if(arguments!=null && arguments.getString(ARGS_COUNTRY)!=null &&
                arguments.getString(ARGS_CITY)!=null &&
                arguments.getString(ARGS_COMPANY)!=null &&
                arguments.getString(ARGS_ITINERARY)!=null &&
                arguments.getString(ARGS_WAY)!=null &&
                arguments.getString(ARGS_TYPEDAY)!=null &&
                arguments.getStringArrayList(ARGS_CODES)!=null) {

            this.country = arguments.getString(ARGS_COUNTRY);
            this.city = arguments.getString(ARGS_CITY);
            this.company = arguments.getString(ARGS_COMPANY);
            this.itinerary = arguments.getString(ARGS_ITINERARY);
            this.way = arguments.getString(ARGS_WAY);
            this.typeday = arguments.getString(ARGS_TYPEDAY);
            this.codesNoSelected = arguments.getStringArrayList(ARGS_CODES);
        }else{
            Log.e(TAG,"null references in args!");
        }
    }

    public void setParams(String country, String city, String company,
                          String itinerary, String way, String typeday,
                          ArrayList<String> codesNoSelected){
        this.country = country;
        this.city = city;
        this.company = company;
        this.itinerary = itinerary;
        this.way = way;
        this.typeday = typeday;
        this.codesNoSelected = codesNoSelected;

    }

    private void initEmptyStateViews(View view) {
        layoutEmptyState = (ViewGroup) view.findViewById(R.id.layoutEmptyState);
        ((TextView)layoutEmptyState.findViewById(R.id.textViewEmptyState)).setText(R.string.no_have_bus);
        ((ImageView)layoutEmptyState.findViewById(R.id.imageViewEmptyState)).setImageDrawable(
                ContextCompat.getDrawable(getActivity(),R.drawable.ic_bench_grey_scale));
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume "+typeday);
        refresh();
    }

    private void initRecyclerView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSchedule);
        recyclerView.setHasFixedSize(true);
        recyclerView.setSelected(true);
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmUseful);
    }


    public void refresh(){

        Log.d(TAG,"codesNoSelected "+codesNoSelected);
        recyclerView.setVisibility(View.INVISIBLE);
        layoutEmptyState.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //SQL
        ScheduleDAO dao = new ScheduleDAO(getContext(),country,city);
        List<Bus> buses = dao.getBuses(company,itinerary,way,typeday);
        dao.close();
        //
        //TODO otimizar
        List<Bus> busesFiltered = new ArrayList<>();
        if(codesNoSelected.size()>0){
            for (Bus bus:buses){
                if(!codesNoSelected.contains(bus.getCode())){
                    busesFiltered.add(bus);
                }
            }
        }else {
            busesFiltered = buses;
        }


        addBus(busesFiltered);
        if(busesFiltered.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            recyclerView.setVisibility(View.INVISIBLE);
            layoutEmptyState.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void addBus(List<Bus> buses){
        listBus = buses;
        Collections.sort(listBus);
        ItemBusAdapter adapterUeful = new ItemBusAdapter(getActivity(), listBus);
        recyclerView.setAdapter(adapterUeful);
    }

    @Override
    public void onStop() {
        listBus.clear();
        super.onStop();
    }
}
