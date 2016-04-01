package br.com.expressobits.hbus;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.expressobits.hbus.backend.Alarm;
import br.com.expressobits.hbus.backend.cityApi.model.City;
import br.com.expressobits.hbus.backend.cityApi.model.GeoPt;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.ui.MainActivity;

/**
 * @author Rafael Correa
 * @since 30/03/16.
 */
public class AlarmDAOAndroidTestUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private static String TAG = "Alarm DAO Test";
    AlarmDAO alarmDAO;
    Context context;
    MainActivity mActivity;

    City city;

    Alarm alarm1;
    Alarm alarm2;
    Alarm alarm2update;


    public AlarmDAOAndroidTestUnitTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();



        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        context = mActivity;
        alarmDAO = new AlarmDAO(context);
        alarm1 = new Alarm();
        alarm1.setId("RS/Santa Maria/Cerrito/Centro > Bairro/12:05");
        alarm1.setTimeAlarm("12:10");
        alarm1.setMinuteDelay(5);
        alarm1.setDaysOfWeek(new ArrayList<String>(Arrays.asList("y", "y", "y", "y", "y", "y", "y")));
        alarm1.setName("Alarm 1 ");

        alarm2 = new Alarm();
        alarm2.setId("RS/Santa Maria/Cerrito/Centro > Bairro/13:05");
        alarm2.setTimeAlarm("13:15");
        alarm2.setMinuteDelay(10);
        alarm2.setDaysOfWeek(new ArrayList<String>(Arrays.asList("y", "n", "n", "y", "y", "y", "y")));
        alarm2.setName("Alarm 2 ");

        alarm2update = new Alarm();
        alarm2update.setId("RS/Santa Maria/Cerrito/Centro > Bairro/13:05");
        alarm2update.setTimeAlarm("13:20");
        alarm2update.setMinuteDelay(15);
        alarm2update.setDaysOfWeek(new ArrayList<String>(Arrays.asList("y", "n", "n", "n", "n", "n", "n")));
        alarm2update.setName("Alarm 2 update ");

        city = new City();
        city.setName("Santa Maria");
        city.setCountry("RS");
        city.setId("RS/Santa Maria");
        city.setLocation(new GeoPt());
    }

    @Test
    public void testInsert() throws Exception {

        alarmDAO.insert(alarm1);
        alarmDAO.insert(alarm2);
        List<Alarm> alarms = new ArrayList<>();
        alarms = alarmDAO.getAlarms(city);
        for (Alarm alarm:alarms) {
            Log.d(TAG,alarm.getId()  + alarm.getName());
        }
        assertEquals(2,alarms.size());
    }

    @Test
    public void testDelete() throws Exception {

        List<Alarm> alarms = new ArrayList<>();
        alarms = alarmDAO.getAlarms(city);
        for (Alarm alarm:alarms) {
            alarmDAO.delete(alarm.getId()  + alarm.getName());
        }

        alarms = alarmDAO.getAlarms(city);
        assertEquals(0,alarms.size());
    }

    @Test
    public void testUpdate() throws Exception {

        //BASIC formation
        alarmDAO.insert(alarm1);
        alarmDAO.insert(alarm2);
        List<Alarm> alarms = new ArrayList<>();
        alarms = alarmDAO.getAlarms(city);
        for (Alarm alarm:alarms) {
            Log.d(TAG,alarm.getId() + alarm.getName());
        }
        assertEquals(2,alarms.size());

        //atualiza o alarm2 para alarm2update
        alarmDAO.insert(alarm2update);
        Alarm alarm = alarmDAO.getAlarm(alarm2.getId()); //USAMOS o alarm 2 para puxar um outro alarme
        assertEquals(alarm.getMinuteDelay(),alarm2update.getMinuteDelay());

    }

}
