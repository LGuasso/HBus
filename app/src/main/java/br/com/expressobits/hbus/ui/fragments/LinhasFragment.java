package br.com.expressobits.hbus.ui.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.adapters.ListViewAdapterFavorite;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.dao.FavoritosDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.OnSettingsListener;
import br.com.expressobits.hbus.utils.Popup;

/**
 * A simple {@link Fragment} subclass.
 * @author Rafael Correa
 * @since 06/07/2015
 */
public class LinhasFragment extends Fragment{

    public String selectedItem;
    ListView listViewLines;
    private ActionButton actionButton;
    private List<Itinerary> itineraries;
    OnSettingsListener mCallback;

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
        initFAB(view);
    }


    private void initListViews(View view){
        listViewLines = (ListView) view.findViewById(R.id.list_lines);


        BusDAO dao  = new BusDAO(getActivity());
        itineraries = dao.getItineraries(true);
        final ListViewAdapterFavorite adapterLinesUteis = new ListViewAdapterFavorite(this.getActivity(),android.R.layout.simple_list_item_1, itineraries);
        listViewLines.setAdapter(adapterLinesUteis);

        //TODO implement background selection one way
        listViewLines.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = itineraries.get(position).getName();
                switch (selectedItem) {
                    case "Itarare Brigada":
                    case "Circular Cemiterio Sul":
                    case "Circular Cemiterio Norte":
                    case "Circular Camobi":
                    case "Circular Barao":
                    case "Brigada Itarare":
                        mCallback.onSettingsDone(selectedItem, Arrays.asList(getActivity().getResources().getStringArray(R.array.list_sentido_circular)).get(0));
                        break;

                    default:
                        Popup.showPopUp(mCallback, view, selectedItem, itineraries.get(position).getSentidos());
                }
            }
        });

        listViewLines.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = itineraries.get(position).getName();

                BusDAO dao = new BusDAO(getActivity());
                Itinerary itinerary = dao.getItinerary(selectedItem);
                itinerary.setFavorite(false);
                dao.update(itinerary);
                onResume();
                return true;
            }
        });
    }

    private void initFAB(View view){
        actionButton = (ActionButton) view.findViewById(R.id.fab_button);
        actionButton.playShowAnimation();
        actionButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));
        actionButton.setButtonColorPressed(getActivity().getResources().getColor(R.color.colorPrimaryDark));

        actionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_DOWN);
        actionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        actionButton.setImageResource(R.drawable.plus);
        actionButton.setOnClickListener((View.OnClickListener)getActivity());
    }

    public void selectedWay(String way){
        mCallback.onSettingsDone(selectedItem, way);
    }


}


