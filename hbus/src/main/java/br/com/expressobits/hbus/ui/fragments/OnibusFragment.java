package br.com.expressobits.hbus.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.RecyclerViewOnClickListenerHack;

/**
 * A simple {@link Fragment} subclass.
 * Que exibe as listas de horarios
 */
public class OnibusFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    public static final String TAG = "OnibusFragment";
    public static final String ARGS_LINHA = "ItineraryId";
    public static final String ARGS_SENTIDO = "Way";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus, container,false);
        initViews(view);

        Bundle arguments = getArguments();
        if(arguments!=null && arguments.getLong(ARGS_LINHA, 0l)!=0l && arguments.getString(ARGS_SENTIDO)!=null){
            refresh(arguments.getLong(ARGS_LINHA),arguments.getString(ARGS_SENTIDO));
        }
        return view;

    }

    private void initViews(View view) {
        initTabLayout(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO implements dia selecionado na tabs
        //TODO calendário de feriados
        //tabs.setCurrentTab(HoursUtils.getTipoDeDia(GregorianCalendar.getInstance()));
    }



    private void initTabLayout(View view){
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        Bundle arguments = getArguments();
        setupViewPager(viewPager, arguments.getLong(ARGS_LINHA), arguments.getString(ARGS_SENTIDO));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager,Long itinearyId,String way) {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity(),
                itinearyId,
                way);
        viewPagerAdapter.addFragment(new HoursFragment(),
                getString(R.string.useful));
        viewPagerAdapter.addFragment(new HoursFragment(),
                getString(R.string.saturday));
        viewPagerAdapter.addFragment(new HoursFragment(),
                getString(R.string.sunday));
        viewPager.setAdapter(viewPagerAdapter);
    }

    /**
     * @param itineraryId
     * @param way
     */
    public void refresh(Long itineraryId,String way){
        viewPagerAdapter.refresh(itineraryId, way);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClickListener(View view, int position) {
        //TODO para imaplementar clickes em horários
        //ItemBusAdapter adapter = (ItemBusAdapter) recyclerViewUsefulDays.getAdapter();
        //adapter.removeListItem(position);
    }

    @Override
    public boolean onLongClickListener(View view, int position) {
        return false;
    }

}
