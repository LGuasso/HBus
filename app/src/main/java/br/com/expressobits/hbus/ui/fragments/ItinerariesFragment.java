package br.com.expressobits.hbus.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.OnSettingsListener;

/**
 * Fragmento que exibe todos {@link br.com.expressobits.hbus.model.Itinerary}
 *
 * <p>Tem comportamento de Callback do fragmento com Activity {@link br.com.expressobits.hbus.ui.MainActivity}</p>
 *
 * @author Rafael Correa
 * @since 16/11/15
 */
public class ItinerariesFragment extends Fragment {

    private ListView listViewItineraries;
    private List<Itinerary> listItineraries;
    private OnSettingsListener onSettingsListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onSettingsListener = (OnSettingsListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " precisa implementar OnSettingsListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_itineraries,null);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle(getString(R.string.all_lines), null);
    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        listViewItineraries = (ListView) view.findViewById(R.id.listViewItineraries);
        BusDAO dao = new BusDAO(getActivity());
        listItineraries = dao.getItineraries();
        ArrayAdapter<Itinerary> arrayAdapter = new ArrayAdapter<Itinerary>(getContext(),android.R.layout.simple_list_item_1);
        listViewItineraries.setAdapter(arrayAdapter);
        listViewItineraries.setClickable(true);
        dao.close();
        listViewItineraries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusDAO dao = new BusDAO(getContext());
                //onSettingsListener.
            }
        });
    }

}
