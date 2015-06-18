package br.com.expressobits.hbus.ui;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.modelo.Linha;
import br.com.expressobits.hbus.utils.Popup;

/**
 * A simple {@link Fragment} subclass.
 */
public class LinhasFragment extends Fragment{

    public String selectedItem;
    ListView listViewLines;
    private ArrayList<Linha> linhas;
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
        initListViews(view);
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
                        Popup.showPopUp(mCallback,view,selectedItem,Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_big_rodoviaria)));
                        break;
                    case "Seletivo UFSM Bombeiros":
                        Popup.showPopUp(mCallback,view,selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_ufsm_bombeiros)));
                        break;
                    case "T Neves Campus":
                        Popup.showPopUp(mCallback,view,selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_tneves_campus)));

                        break;
                    case "Boi Morto":
                        Popup.showPopUp(mCallback,view,selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_boi_morto)));
                        break;
                    case "Carolina Sao Jose":
                        Popup.showPopUp(mCallback,view,selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_carolina_sao_jose)));
                        break;
                    case "Itarare Brigada":
                    case "Circular Cemiterio Sul":
                    case "Circular Cemiterio Norte":
                    case "Circular Camobi":
                    case "Circular Barao":
                    case "Brigada Itarare":
                        mCallback.onSettingsDone(selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_circular)).get(0));
                        break;
                    case "UFSM Circular":
                    case "UFSM":
                    case "Seletivo UFSM":
                        Popup.showPopUp(mCallback,view,selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_ufsm_centro)));
                        break;
                    case "UFSM Bombeiros":
                        Popup.showPopUp(mCallback,view,selectedItem,Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_ufsm_bombeiros)));
                        break;
                    default:
                        Popup.showPopUp(mCallback,view,selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido)));
                }
            }
        });
    }

    public void selectedWay(String way){
        mCallback.onSettingsDone(selectedItem, way);
    }


}


