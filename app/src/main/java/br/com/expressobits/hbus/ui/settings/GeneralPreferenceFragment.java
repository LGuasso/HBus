package br.com.expressobits.hbus.ui.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import br.com.expressobits.hbus.R;

/**
 * @author Rafael Correa
 * @since 16/10/15
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreferenceFragment extends PreferenceFragment {

    Preference preferenceCity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        preferenceCity = findPreference(SelectCityActivity.TAG);
        setHasOptionsMenu(true);
        refreshComponents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshComponents();
    }

    private void refreshComponents() {
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("city"));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("time_home_screen"));
    }
}
