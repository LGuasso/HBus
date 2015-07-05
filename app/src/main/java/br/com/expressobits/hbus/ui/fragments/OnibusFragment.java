package br.com.expressobits.hbus.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.BusAdapter;
import br.com.expressobits.hbus.adapters.ListViewAdapterBus;
import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.Linha;
import br.com.expressobits.hbus.modelo.Bus;
import br.com.expressobits.hbus.modelo.TipoDeDia;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 * Que exibe as listas de horários
 */
public class OnibusFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    public static final String ARGS_LINHA = "Linha";
    public static final String ARGS_SENTIDO = "Sentido";
    RecyclerView recyclerViewUsefulDays;
    RecyclerView recyclerViewSaturday;
    RecyclerView recyclerViewSunday;
    List<Bus> listAuxBusUsefulDays;
    List<Bus> listAuxBusSaturday;
    List<Bus> listAuxBusSunday;

    int contUsefulDays=0;
    int contSaturday=0;
    int contSunday=0;

    List<Bus> listBusUsefulDays;
    List<Bus> listBusSaturday;
    List<Bus> listBusSunday;

    TabHost abas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onibus, null);
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
        recyclerViewSaturday = (RecyclerView)view.findViewById(R.id.rv_listline_line_saturday);
        recyclerViewSaturday.setHasFixedSize(true);
        recyclerViewSunday = (RecyclerView)view.findViewById(R.id.rv_listline_line_sunday);
        recyclerViewSunday.setHasFixedSize(true);

        recyclerViewUsefulDays.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });


        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewUsefulDays.setLayoutManager(llmUseful);

        recyclerViewSaturday.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        LinearLayoutManager llmSaturday = new LinearLayoutManager(getActivity());
        llmSaturday.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSaturday.setLayoutManager(llmSaturday);

        recyclerViewSunday.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /**LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                 BusAdapter adapter = (BusAdapter) recyclerView.getAdapter();

                 if (listBusSunday.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                 //TODO aonde vem meus dados?
                 //List<Bus> listAux = getSetBusList(10);
                 List<Bus> listAux = listBusSunday;

                 for (int i = 0; i < listAux.size(); i++) {
                 adapter.addListItem(listAux.get(i), listBusSunday.size());
                 }
                 }*/
            }
        });


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
            // Que eh o mesmo que
            //View child = tabWidget.getChildAt(i);
            // Vide o codigo fonte

            // O Drawable vai variar conforme o nome do seu tema, confirme se tem
            // algum nome parecido com esse e altere aqui
            //child.setBackgroundResource(R.drawable.tab_indicator_ab_orange);
        }

        abas.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            }
        });
    }

    public void refresh(String linha,String sentido){
        linha = toSimpleName(linha);
        sentido = toSimpleName(sentido);

        listBusUsefulDays = new LinhaFile(getActivity()).getOnibuses(linha,sentido,TipoDeDia.UTEIS);
        BusAdapter adapterUeful = new BusAdapter(getActivity(), listBusUsefulDays);
        adapterUeful.setRecyclerViewOnClickListenerHack(this);
        recyclerViewUsefulDays.setAdapter(adapterUeful);

        listBusSaturday = new LinhaFile(getActivity()).getOnibuses(linha,sentido,TipoDeDia.SABADO);
        BusAdapter adapterSaturday = new BusAdapter(getActivity(), listBusSaturday);
        adapterSaturday.setRecyclerViewOnClickListenerHack(this);
        recyclerViewSaturday.setAdapter(adapterSaturday);

        listBusSunday = new LinhaFile(getActivity()).getOnibuses(linha,sentido,TipoDeDia.DOMINGO);
        BusAdapter adapterSunday = new BusAdapter(getActivity(), listBusSunday);
        adapterSunday.setRecyclerViewOnClickListenerHack(this);
        recyclerViewSunday.setAdapter(adapterSunday);
    }

    @Override
    public void onClickListener(View view, int position) {
        BusAdapter adapter = (BusAdapter) recyclerViewUsefulDays.getAdapter();
        adapter.removeListItem(position);
    }

    public List<Bus> getSetBusList(int tipoDeDia,int qtd,String linha,String sentido){
        List<Bus> listAux = new ArrayList<Bus>();
        List<Bus> list = new ArrayList<Bus>();
        switch (tipoDeDia){
            case 1:
                list = new LinhaFile(getActivity()).getOnibuses(linha, sentido, TipoDeDia.SABADO);
                if(contSaturday+10>list.size()-1){
                    listAux = list.subList(contSaturday,list.size()-1);
                }else{
                    listAux = list.subList(contSaturday, contSaturday + 10);
                }

                contSaturday+=10;
                break;
            case 2:
                list = new LinhaFile(getActivity()).getOnibuses(linha, sentido, TipoDeDia.DOMINGO);
                if(contSunday+10>list.size()-1){
                    listAux = list.subList(contSunday,list.size()-1);
                }else{
                    listAux = list.subList(contSunday, contSunday + 10);
                }
                contSunday+=10;
                break;
            default:
                list = new LinhaFile(getActivity()).getOnibuses(linha, sentido, TipoDeDia.UTEIS);
                if(contUsefulDays+10>list.size()-1){
                    listAux = list.subList(contUsefulDays,list.size()-1);
                }else{
                    listAux = list.subList(contUsefulDays, contUsefulDays + 10);
                }
                contUsefulDays+=10;
                break;

        }

        return(listAux);
    }


    /**
     * Retorna nome sem espaço sem acentos e sem maiusculas.
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
            ok = ok.replace("é", "e");
            ok = ok.toLowerCase();
            return ok;
        }
    }



}
