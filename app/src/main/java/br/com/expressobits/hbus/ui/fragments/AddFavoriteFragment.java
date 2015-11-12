package br.com.expressobits.hbus.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.dao.BusDAO;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.OnSettingsListener;

/**
 * Created by Rafael on 06/07/2015.
 */
public class AddFavoriteFragment extends Fragment{

    ListView listViewLines;
    private ArrayList<Itinerary> itinerarios;
    OnSettingsListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        ((MainActivity)getActivity()).setActionBarTitle(getResources().getString(R.string.add_favorite), "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_favorite, null);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        listViewLines = (ListView) view.findViewById(R.id.list_lines_all);

        BusDAO dao = new BusDAO(getActivity());
        itinerarios = new ArrayList<>(dao.getItineraries(false));
        ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(getActivity(),android.R.layout.simple_list_item_1,itinerarios);
        listViewLines.setAdapter(arrayAdapter);
        listViewLines.setClickable(true);
        dao.close();


        listViewLines.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusDAO dao = new BusDAO(AddFavoriteFragment.this.getActivity());
                Toast.makeText(getActivity(),R.string.added_favorite_itinerary_with_sucess,Toast.LENGTH_SHORT).show();
                itinerarios.get(position).setFavorite(true);
                dao.update(itinerarios.get(position));
                mCallback.onSettingsDone(false);
                mCallback.onAddFavorite();
                dao.close();
            }
        });

    }


}
