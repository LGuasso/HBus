package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView textViewEmptyState;
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
    private String country;
    private String city;
    private String company;
    private String itinerary;
    private String way;
    private String typeday;

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
        textViewEmptyState = (TextView) view.findViewById(R.id.textViewEmptyState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listBus = new ArrayList<>();
        Bundle arguments = getArguments();
        if(arguments!=null && arguments.getString(ARGS_COUNTRY)!=null &&
                arguments.getString(ARGS_CITY)!=null &&
                arguments.getString(ARGS_COMPANY)!=null &&
                arguments.getString(ARGS_ITINERARY)!=null &&
                arguments.getString(ARGS_WAY)!=null &&
                arguments.getString(ARGS_TYPEDAY)!=null){
            this.country = arguments.getString(ARGS_COUNTRY);
            this.city = arguments.getString(ARGS_CITY);
            this.company = arguments.getString(ARGS_COMPANY);
            this.itinerary = arguments.getString(ARGS_ITINERARY);
            this.way = arguments.getString(ARGS_WAY);
            this.typeday = arguments.getString(ARGS_TYPEDAY);
            //refresh(country, city, company, itinerary, way ,typeday);
        }else{
            Log.e(TAG,"null references in args!");
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        refresh(country, city, company, itinerary, way ,typeday);
    }

    private void initRecyclerView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSchedule);
        recyclerView.setHasFixedSize(true);
        recyclerView.setSelected(true);
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmUseful);
    }


    protected void refresh(final String country, final String city, final String company, final String itinerary, final String way, final String typeday){
        recyclerView.setVisibility(View.INVISIBLE);
        textViewEmptyState.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //SQL
        ScheduleDAO dao = new ScheduleDAO(getContext(),country,city);
        List<Bus> buses = dao.getBuses(company,itinerary,way,typeday);
        dao.close();
        //

        addBus(buses);
        if(buses.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            textViewEmptyState.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            recyclerView.setVisibility(View.INVISIBLE);
            textViewEmptyState.setVisibility(View.VISIBLE);
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
