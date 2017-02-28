package br.com.expressobits.hbus.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.provider.ItinerarySearchableProvider;

/**
 * @author Rafael Correa
 * @since 16/10/15
 */
public class PrivacyPreferenceFragment extends PreferenceFragment{

    public static final String PREFERENCE_CLEAR_HISTORY_SEARCH = "clear_history_search";
    public static final String PREFERENCE_PAUSE_SEARCH_HISTORY = "pause_search_history";
    Preference preferenceClearSearchHistory;
    SwitchPreference preferencePauseSearchHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_privacy);
        preferenceClearSearchHistory = findPreference(PREFERENCE_CLEAR_HISTORY_SEARCH);
        preferenceClearSearchHistory.setOnPreferenceClickListener(preference -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_alert_title_confirm_clear_search_history);
            builder.setNegativeButton(android.R.string.no, null);
            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(getActivity(),
                        ItinerarySearchableProvider.AUTHORITY,
                        ItinerarySearchableProvider.MODE);
                searchRecentSuggestions.clearHistory();
                Toast.makeText(getActivity(),getString(R.string.search_history_deleted), Toast.LENGTH_SHORT).show();
            });
            builder.show();

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
        //SettingsActivity.bindPreferenceSummaryToValue(findPreference(PREFERENCE_CLEAR_HISTORY_SEARCH));
        //SettingsActivity.bindPreferenceSummaryToValue(findPreference(PREFERENCE_PAUSE_SEARCH_HISTORY));
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
