package br.com.expressobits.hbus.alarm;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import br.com.expressobits.hbus.backend.Alarm;
import br.com.expressobits.hbus.dao.AlarmDAO;

/**
 * @author Rafael Correa
 * @since 21/04/16
 */
public class BootService extends IntentService{

    public BootService() {
        super("BootService");
    }

    private void setAlarm() {
        // Set your alarm here as you do in "1. First I set an alarm in alarm manager"
    }

    private void setAlarmsFromDatabase() {

        AlarmDAO alarmDAO = new AlarmDAO(this);
        List<Alarm> alarms = alarmDAO.getAlarms();
        for(Alarm alarm:alarms){
            Alarms.saveAlarmInManager(alarm,this);
        }

        // Set your alarms from database here
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        setAlarm();
        setAlarmsFromDatabase(); // A nice a approach is to store alarms on a database, you may not need it
        Intent service = new Intent(this, BootService.class);
        stopService(service);
    }

}
