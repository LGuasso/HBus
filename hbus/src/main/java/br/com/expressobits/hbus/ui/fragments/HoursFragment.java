package br.com.expressobits.hbus.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.adapters.ItemBusAdapter;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.views.SimpleDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class HoursFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    private RecyclerView recyclerView;
    private List<Bus> listBus;
    private Context context;
    private static final String TAG = "HoursFragment";
    public static final String ARGS_LINHA = "ItineraryId";
    public static final String ARGS_SENTIDO = "Way";
    public static final String ARGS_TYPEDAY = "Typeday";

    public HoursFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hours, container, false);
        this.context = inflater.getContext();
        initRecyclerView(view);
        Bundle arguments = getArguments();
        if(arguments!=null && arguments.getLong(ARGS_LINHA, 0l)!=0l &&
                arguments.getString(ARGS_SENTIDO)!=null &&
                arguments.getString(ARGS_TYPEDAY)!=null){
            refresh(context,
                    arguments.getLong(ARGS_LINHA),
                    arguments.getString(ARGS_SENTIDO),
                    arguments.getString(ARGS_TYPEDAY));
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecyclerView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_listline_line);
        recyclerView.setHasFixedSize(true);
        recyclerView.setSelected(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llmUseful);

    }

    protected void refresh(Context context,Long itineraryId,String way,String typeday){
        TimesDbHelper timesDbHelper = new TimesDbHelper(context);
        listBus = timesDbHelper.getBuses(itineraryId,way,typeday);
        timesDbHelper.close();
        ItemBusAdapter adapterUeful = new ItemBusAdapter(context, listBus);
        adapterUeful.setRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapterUeful);

    }

    @Override
    public void onClickListener(View view, int position) {

    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
