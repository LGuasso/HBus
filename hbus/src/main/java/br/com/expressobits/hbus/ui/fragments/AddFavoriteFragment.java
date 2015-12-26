package br.com.expressobits.hbus.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.dao.FavoriteDAO;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;
import br.com.expressobits.hbus.adapters.ItemItineraryAdapter;
import br.com.expressobits.hbus.dao.TimesDbHelper;
import br.com.expressobits.hbus.model.Itinerary;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.OnSettingsListener;
import br.com.expressobits.hbus.ui.settings.SelectCityActivity;
import br.com.expressobits.hbus.ui.views.SimpleDividerItemDecoration;

/**
 * @author Rafael
 * @since 06/07/2015.
 * Fragmento que exibe todos itiner�rios que n�o forma adicionados.
 */
public class AddFavoriteFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    public static final String TAG = "AddFavoriteFragment";
    private ArrayList<Itinerary> itineraries;
    OnSettingsListener onSettingsListener;

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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle(getResources().getString(R.string.add_favorite), "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_favorite,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        initListViews(view);
    }

    private void initListViews(View view){
        RecyclerView recyclerViewItineraries = (RecyclerView) view.findViewById(R.id.recyclerViewAddItineraries);
        TimesDbHelper dao = new TimesDbHelper(getActivity());
        //TODO implementar linhas que ainda n existem no FAVORITeDAO favoritos
        Long cityId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getLong(SelectCityActivity.TAG, 0l);
        itineraries = new ArrayList<>(dao.getItineraries(cityId));

        FavoriteDAO favoriteDAO = new FavoriteDAO(getActivity());

        ItemItineraryAdapter arrayAdapter = new ItemItineraryAdapter(getActivity(),false,itineraries,favoriteDAO.getItineraries(cityId));
        arrayAdapter.setRecyclerViewOnClickListenerHack(this);
        recyclerViewItineraries.setAdapter(arrayAdapter);
        recyclerViewItineraries.setClickable(true);
        recyclerViewItineraries.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        LinearLayoutManager llmUseful = new LinearLayoutManager(getActivity());
        llmUseful.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItineraries.setLayoutManager(llmUseful);
        dao.close();
    }

    @Override
    public void onClickListener(View view, int position) {
        TimesDbHelper dao = new TimesDbHelper(AddFavoriteFragment.this.getActivity());
        FavoriteDAO favoriteDAO = new FavoriteDAO(AddFavoriteFragment.this.getActivity());
        favoriteDAO.insert(itineraries.get(position));
        onSettingsListener.onPopStackBack();
        onSettingsListener.onAddFavorite();
        dao.close();
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }
}
