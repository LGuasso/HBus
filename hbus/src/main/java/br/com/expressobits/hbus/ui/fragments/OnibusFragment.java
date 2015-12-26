package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemBusAdapter;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.ui.views.SimpleDividerItemDecoration;
import br.com.expressobits.hbus.utils.TextUtils;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * A simple {@link Fragment} subclass.
 * Que exibe as listas de horarios
 */
public class OnibusFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    public static final String TAG = "OnibusFragment";
    public static final String ARGS_LINHA = "ItineraryId";
    public static final String ARGS_SENTIDO = "Way";
    RecyclerView recyclerViewUsefulDays;
    RecyclerView recyclerViewSaturday;
    RecyclerView recyclerViewSunday;

    List<Bus> listBusUsefulDays;
    List<Bus> listBusSaturday;
    List<Bus> listBusSunday;

    TabHost abas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus, container,false);
        initViews(view);

        Bundle arguments = getArguments();
        if(arguments!=null && arguments.getLong(ARGS_LINHA, 0l)!=0l && arguments.getString(ARGS_SENTIDO)!=null){
            refresh(arguments.getLong(ARGS_LINHA),arguments.getString(ARGS_SENTIDO));
        }
        return view;

    }

    private void initViews(View view) {
        initTabs(view);
        initRecyclerViews(view);
        //initListViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        abas.setCurrentTab(HoursUtils.getTipoDeDia(GregorianCalendar.getInstance()));
    }

    /**
     * Inicizaliza os recycler views
     * @param view View pai
     */
    private void initRecyclerViews(View view){
        recyclerViewUsefulDays = (RecyclerView)view.findViewById(R.id.rv_listline_line_useful_days);
        recyclerViewUsefulDays.setHasFixedSize(true);
        recyclerViewUsefulDays.setSelected(true);
        recyclerViewUsefulDays.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerViewSaturday = (RecyclerView)view.findViewById(R.id.rv_listline_line_saturday);
        recyclerViewSaturday.setHasFixedSize(true);
        recyclerViewSaturday.setSelected(true);
        recyclerViewSaturday.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerViewSunday = (RecyclerView)view.findViewById(R.id.rv_listline_line_sunday);
        recyclerViewSunday.setHasFixedSize(true);
        recyclerViewSunday.setSelected(true);
        recyclerViewSunday.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));


        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewUsefulDays.setLayoutManager(llmUseful);


        LinearLayoutManager llmSaturday = new LinearLayoutManager(getActivity());
        llmSaturday.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSaturday.setLayoutManager(llmSaturday);


        LinearLayoutManager llmSunday = new LinearLayoutManager(getActivity());
        llmSunday.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSunday.setLayoutManager(llmSunday);

    }

    private void initTabs(View view) {
        abas = (TabHost) view.findViewById(R.id.tabhost);
        abas.setup();
        TabHost.TabSpec descritor = abas.newTabSpec("tag1");
        descritor.setContent(R.id.linear_layout_uteis);
        descritor.setIndicator(getString(R.string.useful));
        abas.addTab(descritor);
        descritor = abas.newTabSpec("tag2");
        descritor.setContent(R.id.linear_layout_sabado);
        descritor.setIndicator(getString(R.string.saturday));
        abas.addTab(descritor);
        descritor = abas.newTabSpec("tag3");
        descritor.setContent(R.id.linear_layout_domingo);
        descritor.setIndicator(getString(R.string.sunday));
        abas.addTab(descritor);
        // Assim como voce mencionou
        TabWidget tabWidget = abas.getTabWidget();
        // Como o TabWidget eh um ViewGroup, ele tem filhos e podemos iterar
        // sobre os mesmos
        int childCount = tabWidget.getChildCount();
        for(int i = 0; i < childCount; ++i) {
            View child = tabWidget.getChildTabViewAt(i);
            TextView tv = (TextView) abas.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(getActivity().getResources().getColor(R.color.white));
            // Que eh o mesmo que
            //View child = tabWidget.getChildAt(i);
            // Vide o codigo fonte

            // O Drawable vai variar conforme o nome do seu tema, confirme se tem
            // algum nome parecido com esse e altere aqui
            child.setBackgroundResource(R.drawable.tab_indicator_ab_blue);
        }

        abas.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            }
        });
    }

    public void refresh(Long itineraryId,String way){
        //linha = toSimpleName(linha);
        //way = TextUtils.toSimpleNameWay(way);
        Log.e(TAG,"Sentido "+way);
        Log.e(TAG,"Linha "+itineraryId);

        TimesDbHelper dao = new TimesDbHelper(getActivity());

        listBusUsefulDays = dao.getBuses(itineraryId,way,TypeDay.USEFUL.toString());

        ItemBusAdapter adapterUeful = new ItemBusAdapter(getActivity(), listBusUsefulDays);
        adapterUeful.setRecyclerViewOnClickListenerHack(this);
        recyclerViewUsefulDays.setAdapter(adapterUeful);

        listBusSaturday = dao.getBuses(itineraryId, way, TypeDay.SATURDAY.toString());
        ItemBusAdapter adapterSaturday = new ItemBusAdapter(getActivity(), listBusSaturday);
        adapterSaturday.setRecyclerViewOnClickListenerHack(this);
        recyclerViewSaturday.setAdapter(adapterSaturday);

        listBusSunday = dao.getBuses(itineraryId, way, TypeDay.SUNDAY.toString());
        ItemBusAdapter adapterSunday = new ItemBusAdapter(getActivity(), listBusSunday);
        adapterSunday.setRecyclerViewOnClickListenerHack(this);
        recyclerViewSunday.setAdapter(adapterSunday);
        dao.close();
    }

    @Override
    public void onClickListener(View view, int position) {
        ItemBusAdapter adapter = (ItemBusAdapter) recyclerViewUsefulDays.getAdapter();
        adapter.removeListItem(position);
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }





}
