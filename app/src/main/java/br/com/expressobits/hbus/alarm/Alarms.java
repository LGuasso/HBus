package br.com.expressobits.hbus.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import br.com.expressobits.hbus.model.Alarm;

/**
 * @author Rafael Correa
 * @since 21/04/16
 */
public class Alarms {

    public static final String TAG = "AlarmsManager";
    /* TODO colocar isto como setttings */
    public static final int MINUTE_OF_VALIDATE_ALARM = 30;

    public static void saveAlarmInManager(Alarm alarm, Context context){
        Intent alarmIntent = new Intent(context,AlarmReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + alarm.getId()));
        alarmIntent.setAction(String.valueOf(alarm.getId()));
        alarmIntent.putExtra(AlarmService.ALARM_ID_KEY,alarm.getId());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent displayIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                alarm.getTimeAlarm(),
                AlarmManager.INTERVAL_DAY,
                displayIntent);
        Log.d(TAG,"Save alarm "+alarm.getTimeAlarm()+" id:"+alarm.getId());
    }

}
