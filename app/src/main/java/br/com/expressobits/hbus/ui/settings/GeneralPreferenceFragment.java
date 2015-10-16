package br.com.expressobits.hbus.ui.settings;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import br.com.expressobits.hbus.R;

/**
 * @author Rafael Correa
 * @since 16/10/15
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);


        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("example_text"));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("city"));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("example_list"));
        SettingsActivity.bindPreferenceSummaryToValue(findPreference("time_home_screen"));
    }
}
