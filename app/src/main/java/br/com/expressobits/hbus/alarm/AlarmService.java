package br.com.expressobits.hbus.alarm;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.ui.notification.NotificationsAlarm;
import br.com.expressobits.hbus.ui.settings.NotificationPreferenceFragment;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael
 * @since 10/04/16.
 */
public class AlarmService extends IntentService{
    public static final String ALARM_ID_KEY = "br.com.expressobits.hbus.ui.alarm.AlarmService.AlarmIdKey";
    private static final String TAG = "AlarmService";

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        AlarmDAO dao = new AlarmDAO(this.getBaseContext());
        String id=intent.getStringExtra(ALARM_ID_KEY);
        Alarm alarm = dao.getAlarm(id);
        sendNotification(alarm);
    }

    private void sendNotification(Alarm alarm) {
        Log.d(TAG,"trying ntification "+alarm.getTimeAlarm());
        Calendar c = Calendar.getInstance();
        Calendar cAlarm = Calendar.getInstance();


        int hour = TimeUtils.getHour(alarm.getTimeAlarm());
        int minute = TimeUtils.getMinute(alarm.getTimeAlarm());


        Log.d(TAG,"send not. "+hour+":"+minute);

        cAlarm.set(Calendar.HOUR_OF_DAY,hour);
        cAlarm.set(Calendar.MINUTE,minute);

        int day = c.get(Calendar.DAY_OF_WEEK);
        boolean isDayToday = false;
        if (day == Calendar.SUNDAY) {
            isDayToday = alarm.isSunday();

        } else if (day == Calendar.MONDAY) {
            isDayToday = alarm.isMonday();

        } else if (day == Calendar.TUESDAY) {
            isDayToday = alarm.isTuesday();

        } else if (day == Calendar.WEDNESDAY) {
            isDayToday = alarm.isWednesday();

        } else if (day == Calendar.THURSDAY) {
            isDayToday = alarm.isThursday();

        } else if (day == Calendar.FRIDAY) {
            isDayToday = alarm.isFriday();

        } else if (day == Calendar.SATURDAY) {
            isDayToday = alarm.isSaturday();

        }

        String minutesToleradosString = PreferenceManager.getDefaultSharedPreferences(this).
                getString(NotificationPreferenceFragment.PREF_NOTIFICATION_TIME_ALARM_AFTER,"15");
        int minutesTolerados = Integer.parseInt(minutesToleradosString);

        Log.d(TAG,"minutes tolerados "+minutesTolerados);

        Log.d(TAG,"c\t"+ TimeUtils.getFormatTime(c));
        Log.d(TAG,"cAlarm\t"+ TimeUtils.getFormatTime(cAlarm));

        if(TimeUtils.isValidAlarm(c,cAlarm,minutesTolerados) && isDayToday){
            NotificationsAlarm.notifyBus(this,alarm);

            Log.d(TAG,"notification alarm "+alarm.toString());
        }


    }



}
