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
import br.com.expressobits.hbus.model.News;
import br.com.expressobits.hbus.ui.alarm.AlarmEditorActivity;
import br.com.expressobits.hbus.ui.settings.NotificationPreferenceFragment;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * @author Rafael Correa
 * @since 14/04/16;
 */
public class NotificationsNews {

    public static void notifyBus(Context context, News news){

        NotificationManager newsNotificationManager;
        Log.d("AlarmService", "Preparing to send notification...: " + news.getId());
        NotificationCompat.Builder alamNotificationBuilder = getBuilderNotification(context, news);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, AlarmEditorActivity.class);
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
        newsNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(NotificationPreferenceFragment.PREF_NOTIFICATION_ALERT_BUS_VIBRATE,true)){
            alamNotificationBuilder.setVibrate(getVibrate(context));
        }


        Notification notification = alamNotificationBuilder.build();
        newsNotificationManager.notify(news.getId(), 1,notification);
        Log.d("NewsService", "Notification sent.");

    }


    private static NotificationCompat.Builder getBuilderNotification(Context context, News news) {

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bus_white_24dp)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
                //.addAction(new NotificationCompat.Action(
                //        R.drawable.ic_close_white_24dp,getResources().getString(R.string.remove),
                //        PendingIntent.getBroadcast(this,0,new Intent(this, HelpActivity.class),0)))
                .setContentTitle(news.getTitle())
                .setContentText(news.getBody())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                //.setSubText(context.getResources().getQuantityText(R.plurals.delay_content, alarm.getMinuteDelay()))
                .setSubText(news.getTime()+"")
                .setTicker(news.getTitle())
                .setSound(getUriRingtone(context))
                .setWhen(System.currentTimeMillis())
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setContentInfo(news.getBody())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                ;
    }

    public static Uri getUriRingtone(Context context) {
        String ringtoneUri =
                PreferenceManager.getDefaultSharedPreferences(context).getString(
                        NotificationPreferenceFragment.PREF_NOTIFICATION_ALERT_BUS_RINGTONE, Settings.System.DEFAULT_NOTIFICATION_URI.getPath());
        Log.d("testSettingRingtone",ringtoneUri);
        Uri uri = Uri.parse(ringtoneUri);
        return uri;
    }

    public static long[] getVibrate(Context context) {
        return (new long[]{ 1000, 0,1000,0});
    }
}
