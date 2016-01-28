package br.com.expressobits.hbus.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.adapters.ItemBusAdapter;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.TypeDay;
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
    public static final String ARGS_CITYID = "cityId";
    public static final String ARGS_ITINERARYID = "itineraryId";
    public static final String ARGS_WAY = "Way";
    public static final String ARGS_TYPEDAY = "typeday";

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
        if(arguments!=null && arguments.getString(ARGS_CITYID)!=null &&
                arguments.getString(ARGS_ITINERARYID)!=null &&
                arguments.getString(ARGS_WAY)!=null &&
                arguments.getString(ARGS_TYPEDAY)!=null){
            refresh(context,
                    arguments.getString(ARGS_CITYID),
                    arguments.getString(ARGS_ITINERARYID),
                    arguments.getString(ARGS_WAY),
                    TypeDay.getTypeday(ARGS_TYPEDAY));
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

    protected void refresh(Context context,String cityId,String itineraryId,String way,TypeDay typeday){
        BusDAO dao = new BusDAO(context);
        listBus = dao.getBuses(cityId,itineraryId,way,typeday);
        dao.close();
        ItemBusAdapter adapterUeful = new ItemBusAdapter(context, listBus,cityId);
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
