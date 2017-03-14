package br.com.expressobits.hbus.ui.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.ui.download.ListDatabaseActivity;

/**
 * @author Rafael Correa
 * @since 16/10/15
 *
 * This fragment shows data and sync preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DataSyncPreferenceFragment extends PreferenceFragment {

    public static String LAST_SYNC_PREFERENCE_KEY = "br.com.expressobits.hbus.ui.settings.last_sync";
    public static String SYNC_FREQUENCY_PREFERENCE_KEY = "br.com.expressobits.hbus.ui.settings.sync_frequency";
    public static String defaultSyncFrequency = "3600000";

    Preference preferenceHBusSchedules;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_data_sync);
        preferenceHBusSchedules = findPreference("hbus_schedules");
        preferenceHBusSchedules.setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(preference.getContext(), ListDatabaseActivity.class));
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
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("hbus_schedules"));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(SYNC_FREQUENCY_PREFERENCE_KEY));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("no_actived_itens"));
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
