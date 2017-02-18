package br.com.expressobits.hbus.ui.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.model.Code;
import br.com.expressobits.hbus.ui.alarm.AlarmEditorActivity;
import br.com.expressobits.hbus.ui.settings.NotificationPreferenceFragment;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * @author Rafael Correa
 * @since 14/04/16
 */
public class NotificationsAlarm {

    public static void notifyBus(Context context,Alarm alarm){

        NotificationManager alarmNotificationManager;
        Log.d("AlarmService", "Preparing to send notification...: " + alarm.getId());
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NotificationPreferenceFragment.PREF_NOTIFICATION_ALERT_BUS,true)){
            NotificationCompat.Builder alamNotificationBuilder = getBuilderNotification(context, alarm);

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, AlarmEditorActivity.class);
            resultIntent.putExtra(AlarmEditorActivity.ARGS_ALARM_ID, alarm.getId());
            resultIntent.putExtra(AlarmEditorActivity.ARGS_ALARM_CODE, alarm.getCode());
            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(AlarmEditorActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            alamNotificationBuilder.setContentIntent(resultPendingIntent);
            alarmNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NotificationPreferenceFragment.PREF_NOTIFICATION_ALERT_BUS_VIBRATE,true)){
                alamNotificationBuilder.setVibrate(getVibrate());
            }

            setCodeToNotification(alarm, alamNotificationBuilder);

            Notification notification = alamNotificationBuilder.build();
            alarmNotificationManager.notify(alarm.getId(), 1,notification);
            Log.d("AlarmService", "Notification sent.");
        }

    }

    private static void setCodeToNotification(Alarm alarm, final NotificationCompat.Builder alamNotificationBuilder) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference codeTableRef  = database.getReference(FirebaseUtils.CODE_TABLE);
        DatabaseReference countryRef = codeTableRef.child(FirebaseUtils.getCountry(alarm.getId()));
        DatabaseReference cityRef = countryRef.child(FirebaseUtils.getCityName(alarm.getId()));
        DatabaseReference companyRef = cityRef.child(FirebaseUtils.getCompany(alarm.getId()));
        DatabaseReference codeRef = companyRef.child(alarm.getCode());
        codeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Code code = dataSnapshot.getValue(Code.class);
                if(code!=null){
                    alamNotificationBuilder.setSubText(code.getDescrition());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private static NotificationCompat.Builder getBuilderNotification(Context context, Alarm alarm) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(FirebaseUtils.getTimeForBus(alarm.getId())));
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bus_white_24dp)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                //.addAction(new NotificationCompat.Action(
                //        R.drawable.ic_close_white_24dp,getResources().getString(R.string.remove),
                //        PendingIntent.getBroadcast(this,0,new Intent(this, HelpActivity.class),0)))
                .setContentTitle(context.getString(R.string.notification_alarm_title_text,
                        FirebaseUtils.getItinerary(alarm.getId()), TimeUtils.getFormatTime(calendar)))
                .setContentText(context.getString(R.string.notification_alarm_text,
                        FirebaseUtils.getWay(alarm.getId())))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //.setSubText(context.getResources().getQuantityText(R.plurals.delay_content, alarm.getMinuteDelay()))
                .setSubText(context.getString(R.string.notification_alarm_subtext,FirebaseUtils.getCompany(alarm.getId())))
                .setTicker(context.getString(R.string.notification_alarm_ticker, FirebaseUtils.getItinerary(alarm.getId())))
                .setSound(getUriRingtone(context))
                .setWhen(System.currentTimeMillis())
                .setColor(ContextCompat.getColor(context,R.color.colorAccent))
                .setContentInfo(alarm.getCode())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                ;
    }

    private static Uri getUriRingtone(Context context) {
        String ringtoneUri =
                PreferenceManager.getDefaultSharedPreferences(context).getString(
                        NotificationPreferenceFragment.PREF_NOTIFICATION_ALERT_BUS_RINGTONE, Settings.System.DEFAULT_NOTIFICATION_URI.getPath());
        Log.d("testSettingRingtone",ringtoneUri);
        return Uri.parse(ringtoneUri);
    }

    private static long[] getVibrate() {
        return (new long[]{ 1000, 0,1000,0});
    }
}
