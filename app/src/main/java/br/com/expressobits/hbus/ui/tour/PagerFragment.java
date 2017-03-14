package br.com.expressobits.hbus.ui.tour;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.expressobits.hbus.R;


public class PagerFragment extends Fragment {

    private static final String ARG_COLOR    = "color";
    private static final String ARG_PAGE     = "page";
    private static final String ARG_TITLE    = "title";
    private static final String ARG_SUBTITLE = "subtitle";
    private static final String ARG_ICON     = "icon";
    private static final String ARG_ICON_BOTTOM = "iconbottom";

    private int mColor;
    private int mPage;
    private String mTitle;
    private String mSubTitle;
    private int mIcon;
    private int mIconBottom;


    public static PagerFragment newInstance(int colorHexa, int page, String title, String subtitle, int icon) {
        PagerFragment fragment = new PagerFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_COLOR, colorHexa);
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_SUBTITLE, subtitle);
        args.putInt(ARG_ICON, icon);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColor = getArguments().getInt(ARG_COLOR);
        }
        if (savedInstanceState != null) {
            mPage       = savedInstanceState.getInt(ARG_PAGE);
            mTitle      = savedInstanceState.getString(ARG_TITLE);
            mSubTitle   = savedInstanceState.getString(ARG_SUBTITLE);
            mIcon       = savedInstanceState.getInt(ARG_ICON);
            mIconBottom = savedInstanceState.getInt(ARG_ICON_BOTTOM);
        } else {
            mPage       = getArguments().getInt(ARG_PAGE, 0);
            mTitle      = getArguments().getString(ARG_TITLE);
            mSubTitle   = getArguments().getString(ARG_SUBTITLE);
            mIcon       = getArguments().getInt(ARG_ICON);
            mIconBottom = getArguments().getInt(ARG_ICON_BOTTOM);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ARG_PAGE, mPage);
        outState.putString(ARG_TITLE, mTitle);
        outState.putString(ARG_SUBTITLE, mSubTitle);
        outState.putInt(ARG_ICON, mIcon);
        outState.putInt(ARG_ICON_BOTTOM, mIconBottom);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTour = inflater.inflate(R.layout.tour_relative, container, false);

        AppCompatImageView iconImage = (AppCompatImageView) viewTour.findViewById(R.id.iconImage);
        TextView titleText = (TextView) viewTour.findViewById(R.id.titleText);
        TextView subtitleText = (TextView) viewTour.findViewById(R.id.subtitleText);

        viewTour.setBackgroundColor(mColor);
        iconImage.setImageResource(mIcon);
        titleText.setText(mTitle);
        subtitleText.setText(mSubTitle);

        return viewTour;
    }
}
