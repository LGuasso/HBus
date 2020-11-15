package br.com.expressobits.hbus.ui.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import br.com.expressobits.hbus.R;

/**
 * @author Rafael Correa
 * @since 16/10/15
 */
/**
 * This fragment shows notification preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NotificationPreferenceFragment extends PreferenceFragment {


    public static final String PREF_NOTIFICATION_ALERT_BUS = "notifications_alert_bus";
    public static final String PREF_NOTIFICATION_ALERT_BUS_RINGTONE = "notifications_alert_bus_ringtone";
    public static final String PREF_NOTIFICATION_ALERT_BUS_VIBRATE = "notifications_alert_bus_vibrate";
    public static final String PREF_NOTIFICATION_TIME_ALARM_AFTER = "time_alarm_after";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_notification);
        setHasOptionsMenu(true);
        SettingsActivity.bindPreferenceSummaryToValue(findPreference(PREF_NOTIFICATION_ALERT_BUS_RINGTONE));
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