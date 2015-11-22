package br.com.expressobits.hbus.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemItineraryAdapter;
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
public class ItinerariesFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView recyclerViewItineraries;
    private List<Itinerary> listItineraries;
    public static final String TAG = "ItinerariesFragment";
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
        recyclerViewItineraries = (RecyclerView) view.findViewById(R.id.recyclerViewItineraries);
        recyclerViewItineraries.setHasFixedSize(true);
        BusDAO dao = new BusDAO(getActivity());
        listItineraries = dao.getItineraries();
        ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(getContext(),listItineraries);
        arrayAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewItineraries.setAdapter(arrayAdapter);

        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItineraries.setLayoutManager(llmUseful);
        dao.close();
    }

    @Override
    public void onClickListener(View view, int position) {
        ((MainActivity)getActivity()).onCreateDialogChooseWay(listItineraries.get(position).getName());
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
