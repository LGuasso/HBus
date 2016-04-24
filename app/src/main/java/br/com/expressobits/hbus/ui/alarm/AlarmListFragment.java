package br.com.expressobits.hbus.ui.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.adapters.ItemAlarmAdapter;
import br.com.expressobits.hbus.backend.Alarm;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.views.SimpleDividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmListFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    public static final String TAG = "AlarmListFragment";
    public static final String ARGS_CITYID = "cityId";

    private String cityId;

    private RecyclerView recyclerViewAlarms;
    private List<Alarm> alarmList;


    public AlarmListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm_list, container, false);
        initViews(view);

        Bundle arguments = getArguments();
        if(arguments!=null && arguments.getString(ARGS_CITYID)!=null){
            cityId = arguments.getString(ARGS_CITYID);
        }
        return view;
    }

    private void initViews(View view){
        initRecyclerViewAlarms(view,getActivity());
    }

    private void updateListAlarms(Context context){
        AlarmDAO alarmDAO = new AlarmDAO(context);
        BusDAO busDAO = new BusDAO(context);
        City city = busDAO.getCity(cityId);
        busDAO.close();
        if(city!=null){
            alarmList = alarmDAO.getAlarms(city);
        }
        alarmDAO.close();
    }

    private void initRecyclerViewAlarms(View view,Context context){
        recyclerViewAlarms = (RecyclerView) view.findViewById(R.id.recyclerViewAlarms);
        recyclerViewAlarms.setHasFixedSize(true);
        recyclerViewAlarms.setSelected(true);
        recyclerViewAlarms.addItemDecoration(new SimpleDividerItemDecoration(context));
        LinearLayoutManager llmUseful = new LinearLayoutManager(context);
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAlarms.setLayoutManager(llmUseful);
    }

    private void udpateRecyclerViewAlarms(Context context){
        ItemAlarmAdapter adapter =
                new ItemAlarmAdapter(context,alarmList,cityId);
        adapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewAlarms.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        updateListAlarms(getActivity());
        udpateRecyclerViewAlarms(getActivity());
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.alarms));
        super.onResume();
    }

    @Override
    public void onClickListener(View view, int position) {
        Intent intent = new Intent(getActivity(), AlarmEditorActivity.class);
        String id = alarmList.get(position).getId();
        intent.putExtra(AlarmEditorActivity.ARGS_ALARM_ID,id);
        startActivity(intent);
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
