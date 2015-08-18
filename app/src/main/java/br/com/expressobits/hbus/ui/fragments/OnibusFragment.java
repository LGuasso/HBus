package br.com.expressobits.hbus.ui.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.BusAdapter;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Bus;
import br.com.expressobits.hbus.model.TypeDay;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 * Que exibe as listas de hor�rios
 */
public class OnibusFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    public static final String ARGS_LINHA = "Line";
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
        View view = inflater.inflate(R.layout.fragment_bus, null);
        initViews(view);

        Bundle arguments = getArguments();
        if(arguments!=null && arguments.getString(ARGS_LINHA)!=null && arguments.getString(ARGS_SENTIDO)!=null){
            refresh(arguments.getString(ARGS_LINHA),arguments.getString(ARGS_SENTIDO));
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
        abas.setCurrentTab(TimeUtils.getTipoDeDia(GregorianCalendar.getInstance()));
    }

    /**
     * Inicizaliza os recycler views
     * @param view
     */
    private void initRecyclerViews(View view){
        recyclerViewUsefulDays = (RecyclerView)view.findViewById(R.id.rv_listline_line_useful_days);
        recyclerViewUsefulDays.setHasFixedSize(true);
        recyclerViewUsefulDays.setSelected(true);
        recyclerViewSaturday = (RecyclerView)view.findViewById(R.id.rv_listline_line_saturday);
        recyclerViewSaturday.setHasFixedSize(true);
        recyclerViewSaturday.setSelected(true);
        recyclerViewSunday = (RecyclerView)view.findViewById(R.id.rv_listline_line_sunday);
        recyclerViewSunday.setHasFixedSize(true);
        recyclerViewSunday.setSelected(true);


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

    public void refresh(String linha,String sentido){
        //linha = toSimpleName(linha);
        sentido = toSimpleName(sentido);

        BusDAO dao = new BusDAO(getActivity());


        listBusUsefulDays = dao.getBusList(linha, sentido, TypeDay.USEFUL.toString(),false);
        BusAdapter adapterUeful = new BusAdapter(getActivity(), listBusUsefulDays);
        adapterUeful.setRecyclerViewOnClickListenerHack(this);
        recyclerViewUsefulDays.setAdapter(adapterUeful);

        listBusSaturday = dao.getBusList(linha, sentido, TypeDay.SATURDAY.toString(),false);
        BusAdapter adapterSaturday = new BusAdapter(getActivity(), listBusSaturday);
        adapterSaturday.setRecyclerViewOnClickListenerHack(this);
        recyclerViewSaturday.setAdapter(adapterSaturday);

        listBusSunday = dao.getBusList(linha, sentido, TypeDay.SUNDAY.toString(),false);
        BusAdapter adapterSunday = new BusAdapter(getActivity(), listBusSunday);
        adapterSunday.setRecyclerViewOnClickListenerHack(this);
        recyclerViewSunday.setAdapter(adapterSunday);
    }

    @Override
    public void onClickListener(View view, int position) {
        BusAdapter adapter = (BusAdapter) recyclerViewUsefulDays.getAdapter();
        adapter.removeListItem(position);
    }



    /**
     * Retorna nome sem espa�o sem acentos e sem maiusculas.
     * @param name
     * @return
     */
    private String toSimpleName(String name){
        if(name == null){
            return "";
        }else {
            String ok = name;
            ok = ok.replace(" > ","-");
            ok = ok.replace(" < ","-");
            ok = ok.replace(" ", "-");
            ok = ok.replace("�", "e");
            ok = ok.toLowerCase();
            return ok;
        }
    }



}
