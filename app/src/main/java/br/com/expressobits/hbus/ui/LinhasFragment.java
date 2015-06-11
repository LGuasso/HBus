package br.com.expressobits.hbus.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.Linha;

/**
 * A simple {@link Fragment} subclass.
 */
public class LinhasFragment extends Fragment {


    public static final String EXTRA_LINE = "line_bus";
    public static final String EXTRA_SENTIDO = "line_sentido";
    Spinner spinnerSentido;
    public String selectedItem;
    ListView listViewLines;
    private ArrayList<Linha> linhas;
    ArrayAdapter<CharSequence> adapter;


    OnSettingsListener mCallback;

    int lastPosition = 0;

    public LinhasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Este código serve para nos certificarmos que a Activity que contém o Fragment
        // implementa a interface do callback. Se não, lança uma exceção
        try {
            mCallback = (OnSettingsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " precisa implementar OnSettingsListener");
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle(getResources().getString(R.string.app_name), "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_linhas, null);
        initViews(view);

        return view;

    }

    private void initViews(View view){
        initButtons(view);
        initSpinners(view);
        initListViews(view);
    }

    private void initButtons(View view) {
        Button button = (Button) view.findViewById(R.id.button_main_look_line);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSettingsDone(selectedItem, spinnerSentido.getSelectedItem().toString());
                //((MainActivity)LinhasFragment.this.getActivity()).addOnibusFragment();
            }
        });
    }


    /**
     * Iniciando os spinners.
     * Cria spinners e define arrayAdapters
     * e define seleção
     */
    private void initSpinners(View view) {
        spinnerSentido = (Spinner) view.findViewById(R.id.spinner_list_sentido);
        adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.list_sentido, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
    }

    private void initListViews(View view){
        listViewLines = (ListView) view.findViewById(R.id.list_lines);
        new LinhaFile(this.getActivity()).iniciarDados();
        linhas = LinhaFile.getLinhas();
        final ListViewAdapterLines adapterLinesUteis = new ListViewAdapterLines(this.getActivity(),android.R.layout.simple_list_item_1, linhas);
        listViewLines.setAdapter(adapterLinesUteis);

        listViewLines.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = linhas.get(position).getNome();
                switch (selectedItem) {
                    case "Big Rodoviaria":
                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido_centro_rodoviaria, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "Seletivo UFSM Bombeiros":
                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido_ufsm_bombeiros, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "T Neves Campus":
                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido_tneves_campus, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "Boi Morto":
                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido_boi_morto, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "Carolina Sao Jose":
                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido_carolina_sao_jose, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "Itarare Brigada":
                    case "Circular Cemiterio Sul":
                    case "Circular Cemiterio Norte":
                    case "Circular Camobi":
                    case "Circular Barao":
                    case "Brigada Itarare":
                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido_circular, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "UFSM Circular":
                    case "UFSM":
                    case "Seletivo UFSM":
                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido_ufsm_centro, android.R.layout.simple_dropdown_item_1line);
                        break;
                    case "UFSM Bombeiros":
                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido_ufsm_bombeiros, android.R.layout.simple_dropdown_item_1line);
                        break;
                    default:

                        adapter = ArrayAdapter.createFromResource(LinhasFragment.this.getActivity(), R.array.list_sentido, android.R.layout.simple_dropdown_item_1line);

                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSentido.setAdapter(adapter);
                spinnerSentido.setSelection(0);
            }
        });
    }
}
