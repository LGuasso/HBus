package br.com.expressobits.hbus.alarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Receiver Alarm
 * @see android.support.v4.content.WakefulBroadcastReceiver
 * @see android.content.BroadcastReceiver
 * @author Rafael Correa
 * @since 10/04/16
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
        //HelpActivity inst = HelpActivity.instance();
        //inst.setAlarmText("Alarm! Wake up! Wake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        /**Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();*/

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        Intent intentService = (intent.setComponent(comp));
        intentService.putExtra(AlarmService.ALARM_ID_KEY,intent.getStringExtra(AlarmService.ALARM_ID_KEY));
        startWakefulService(context,intentService);
        setResultCode(Activity.RESULT_OK);
    }
}
