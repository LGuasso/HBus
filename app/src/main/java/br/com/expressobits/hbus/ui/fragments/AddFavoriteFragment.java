package br.com.expressobits.hbus.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.adapters.ListViewAdapterFavorite;
import br.com.expressobits.hbus.dao.FavoritosDAO;
import br.com.expressobits.hbus.file.LinhaFile;
import br.com.expressobits.hbus.ui.MainActivity;
import br.com.expressobits.hbus.ui.OnSettingsListener;
import br.com.expressobits.hbus.utils.Popup;

/**
 * Created by Rafael on 06/07/2015.
 */
public class AddFavoriteFragment extends Fragment {

    ListView listViewLines;
    private ArrayList<String> nomeItinerarios;
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

        nomeItinerarios = new ArrayList<>(new LinhaFile(getActivity()).getNomeLinhas());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,nomeItinerarios);
        listViewLines.setAdapter(arrayAdapter);
        listViewLines.setClickable(true);

        final FavoritosDAO dao = new FavoritosDAO(getActivity());
        listViewLines.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dao.inserirOuAlterar(nomeItinerarios.get(position));
            }
        });

    }

}
