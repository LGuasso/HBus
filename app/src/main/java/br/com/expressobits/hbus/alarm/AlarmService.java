package br.com.expressobits.hbus.alarm;

import android.app.IntentService;
import android.content.Intent;

import java.util.Calendar;

import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.ui.Notifications;
import br.com.expressobits.hbus.utils.HoursUtils;

/**
 * @author Rafael
 * @since 10/04/16.
 */
public class AlarmService extends IntentService{
    public static final String ALARM_ID_KEY = "br.com.expressobits.hbus.ui.alarm.AlarmService.AlarmIdKey";

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
        Calendar c = Calendar.getInstance();
        Calendar cAlarm = HoursUtils.getTimeInCalendar(alarm.getTimeAlarm());
        cAlarm.add(Calendar.MINUTE,Alarms.MINUTE_OF_VALIDATE_ALARM);

        int day = c.get(Calendar.DAY_OF_WEEK);
        boolean isDayToday = false;
        switch (day){
            case 1:
                isDayToday = alarm.isSunday();
                break;
            case 2:
                isDayToday = alarm.isMonday();
                break;
            case 3:
                isDayToday = alarm.isTuesday();
                break;
            case 4:
                isDayToday = alarm.isWednesday();
                break;
            case 5:
                isDayToday = alarm.isThursday();
                break;
            case 6:
                isDayToday = alarm.isFriday();
                break;
            case 7:
                isDayToday = alarm.isSaturday();
                break;
        }

        if(cAlarm.compareTo(c)>-1 && isDayToday){
            Notifications.notifyBus(this,alarm);
        }


    }
}
