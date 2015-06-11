package br.com.expressobits.hbus.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.Onibus;
import br.com.expressobits.hbus.modelo.TipoDeDia;

/**
 * A simple {@link Fragment} subclass.
 * Que exibe as listas de horários
 */
public class OnibusFragment extends Fragment{

    public static final String ARGS_LINHA = "Linha";
    public static final String ARGS_SENTIDO = "Sentido";

    ListView listViewUseful;
    ListView listViewSaturday;
    ListView listViewSunday;
    TabHost abas;

    public OnibusFragment() {
        // Required empty public constructor
    }

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
        initListViews(view);
    }

    private void initListViews(View view) {

        listViewUseful = (ListView)view.findViewById(R.id.listview_listline_line_uteis);

        listViewSaturday = (ListView)view.findViewById(R.id.listview_listline_line_sabado);

        listViewSunday = (ListView)view.findViewById(R.id.listview_listline_line_domingo);
    }

    private void initTabs(View view) {
        abas = (TabHost) view.findViewById(R.id.tabhost);
        abas.setup();

        //TODO Definir pelo dia retornado
        abas.setCurrentTab(0);

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
            child.setBackgroundResource(R.drawable.tab_indicator_ab_orange);
        }
    }

    protected void refresh(String linha,String sentido){
        linha = toSimpleName(linha);
        sentido = toSimpleName(sentido);
        ArrayList<Onibus> listUseful = new LinhaFile(this.getActivity()).getOnibuses(linha, sentido, TipoDeDia.UTEIS);
        ListViewAdapterBus adapterLinesUteis = new ListViewAdapterBus(this.getActivity(),android.R.layout.simple_list_item_1, listUseful);
        listViewUseful.setAdapter(adapterLinesUteis);

        ArrayList<Onibus> listSabado = new LinhaFile(this.getActivity()).getOnibuses(linha, sentido, TipoDeDia.SABADO);
        ListViewAdapterBus adapterLinesSabado = new ListViewAdapterBus(this.getActivity(),android.R.layout.simple_list_item_1, listSabado);
        listViewSaturday.setAdapter(adapterLinesSabado);

        ArrayList<Onibus> listDomingo = new LinhaFile(this.getActivity()).getOnibuses(linha, sentido, TipoDeDia.DOMINGO);
        ListViewAdapterBus adapterLinesDomingo = new ListViewAdapterBus(this.getActivity(),android.R.layout.simple_list_item_1, listDomingo);
        listViewSunday.setAdapter(adapterLinesDomingo);
    }

    /**
     * Retorna nome sem espaço sem acentos e sem maiusculas.
     * @param name
     * @return
     * TODO ver o null deste método
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
