package br.com.expressobits.hbus.ui.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.City;
import br.com.expressobits.hbus.ui.adapters.ItemAlarmAdapter;
import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.utils.FirebaseUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmListFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    public static final String TAG = "AlarmListFragment";

    private RecyclerView recyclerViewAlarms;
    private RelativeLayout relativeLayoutEmptyState;
    private List<Alarm> alarmList = new ArrayList<>();


    public AlarmListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_alarm_list, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        initRecyclerViewAlarms(view,getActivity());
        relativeLayoutEmptyState = (RelativeLayout)  view.findViewById(R.id.relativeLayoutEmptyList);
    }


    private void initRecyclerViewAlarms(View view,Context context){
        recyclerViewAlarms = (RecyclerView) view.findViewById(R.id.recyclerViewAlarms);
        recyclerViewAlarms.setHasFixedSize(true);
        recyclerViewAlarms.setSelected(true);
        LinearLayoutManager llmUseful = new LinearLayoutManager(context);
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewAlarms.setLayoutManager(llmUseful);
        ItemAlarmAdapter adapter =
                new ItemAlarmAdapter(context,alarmList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewAlarms.setAdapter(adapter);
    }

    private void updateListAlarms(Context context){
        AlarmDAO alarmDAO = new AlarmDAO(context);
        String cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(SelectCityActivity.TAG,SelectCityActivity.NOT_CITY);
        City city= new City();
        city.setName(FirebaseUtils.getCityName(cityId));
        city.setCountry(FirebaseUtils.getCountry(cityId));
        alarmList = alarmDAO.getAlarms(city);
        alarmDAO.close();
    }

    private void updateRecyclerViewAlarms(){
        ItemAlarmAdapter itemAlarmAdapter = new ItemAlarmAdapter(getActivity(),alarmList);
        itemAlarmAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewAlarms.setAdapter(itemAlarmAdapter);
    }

    @Override
    public void onResume() {
        updateListAlarms(getActivity());
        updateRecyclerViewAlarms();
        if(alarmList.size()>0){
            recyclerViewAlarms.setVisibility(View.VISIBLE);
            relativeLayoutEmptyState.setVisibility(View.INVISIBLE);
        }else {
            recyclerViewAlarms.setVisibility(View.INVISIBLE);
            relativeLayoutEmptyState.setVisibility(View.VISIBLE);
        }

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
