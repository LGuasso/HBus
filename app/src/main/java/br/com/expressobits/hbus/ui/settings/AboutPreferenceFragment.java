package br.com.expressobits.hbus.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.tour.TourActivity;

/**
 * @author Rafael Correa
 * @since 16/10/15
 */
public class AboutPreferenceFragment extends PreferenceFragment{

    Preference preferenceTour;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_about);
        preferenceTour = findPreference("tour");
        preferenceTour.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(preference.getContext(), TourActivity.class));
            return false;
        });
        setHasOptionsMenu(true);
        refreshComponents();
    }

    private void refreshComponents() {
        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("tour"));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("debug"));
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
}
