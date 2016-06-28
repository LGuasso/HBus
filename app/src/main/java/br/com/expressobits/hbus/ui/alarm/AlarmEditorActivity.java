package br.com.expressobits.hbus.ui.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.com.expressobits.hbus.R;
import br.com.expressobits.hbus.alarm.AlarmReceiver;
import br.com.expressobits.hbus.alarm.AlarmService;
import br.com.expressobits.hbus.alarm.Alarms;
import br.com.expressobits.hbus.model.Alarm;
import br.com.expressobits.hbus.dao.AlarmDAO;
import br.com.expressobits.hbus.utils.DAOUtils;
import br.com.expressobits.hbus.utils.FirebaseUtils;
import br.com.expressobits.hbus.utils.TextUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AlarmEditorActivity extends AppCompatActivity {

    public static final String ARGS_ALARM_ID = "br.com.expressobits.hbus.ui.AlarmIdKey";
    public static final String ARGS_ALARM_CODE = "br.com.expressobits.hbus.ui.AlarmCodeKey";
    private Alarm alarm;
    private boolean editing;
    private Toolbar toolbar;
    private TextView textViewTime;
    private SwitchCompat switchActived;
    private Spinner spinnerDelayType;
    private CheckBox checkBoxSunday;
    private CheckBox checkBoxMonday;
    private CheckBox checkBoxTuesday;
    private CheckBox checkBoxWednesday;
    private CheckBox checkBoxThursday;
    private CheckBox checkBoxFriday;
    private CheckBox checkBoxSaturday;
    private EditText editTextName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_editor);
        initViews();
        if(loadAlarm()){
            setTitle(getString(R.string.edit_alarm));
        }else {
            setTitle(getString(R.string.new_alarm));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        AlarmDAO alarmDAO;
        switch (id){

            case R.id.menu_action_save:
                alarmDAO = new AlarmDAO(this);
                saveAlarm();
                if(alarmDAO.insert(alarm)){
                    Toast.makeText(this, R.string.add_new_alarm_with_sucess,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, R.string.updated_alarm_with_sucess,Toast.LENGTH_SHORT).show();
                }
                finish();
                break;

            case R.id.menu_action_remove:
                alarmDAO = new AlarmDAO(this);
                removeAlarmManager();
                alarmDAO.delete(alarm.getId());
                Toast.makeText(this,"Remove alarm "+alarm.getId(),Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean loadAlarm(){
        AlarmDAO alarmDAO = new AlarmDAO(this);
        String alarmId = getIntent().getStringExtra(ARGS_ALARM_ID);
        String code = getIntent().getStringExtra(ARGS_ALARM_CODE);
        alarm = alarmDAO.getAlarm(alarmId);
        editing = alarm!=null;
        if(!editing){
            alarm = new Alarm();
            alarm.setId(alarmId);
            switchActived.setChecked(true);
            /**TODO fazer seletor de typday para aparecer no novo alarme ja os dias*/

        }else {
            switchActived.setChecked(alarm.isActived());
        }
        int delay = alarm.getMinuteDelay();


        spinnerDelayType.setSelection((10+delay)/5);


        /**switch (delay){
            case -10:
                spinnerDelayType.setSelection(0);
                break;
            case -5:
                spinnerDelayType.setSelection(1);
                break;
            case 0:
                spinnerDelayType.setSelection(2);
                break;
            case 5:
                spinnerDelayType.setSelection(3);
                break;
            case 10:
                spinnerDelayType.setSelection(4);
                break;
        }*/
        editTextName.setText(alarm.getName());
        textViewTime.setText(FirebaseUtils.getTimeForBus(alarm.getId()));
        resumeWeekDays();
        alarm.setCode(code);
        return editing;
    }

    private void saveAlarm(){
        alarm.setName(editTextName.getText().toString());
        alarm.setActived(switchActived.isChecked());
        alarm.setSunday(checkBoxSunday.isChecked());
        alarm.setMonday(checkBoxMonday.isChecked());
        alarm.setTuesday(checkBoxTuesday.isChecked());
        alarm.setWednesday(checkBoxWednesday.isChecked());
        alarm.setThursday(checkBoxThursday.isChecked());
        alarm.setFriday(checkBoxFriday.isChecked());
        alarm.setSaturday(checkBoxSaturday.isChecked());
        alarm.setMinuteDelay(-10+(5*spinnerDelayType.getSelectedItemPosition()));
        /*switch (spinnerDelayType.getSelectedItemPosition()){
            case 0:
                alarm.setMinuteDelay(-10);
                break;
            case 1:
                alarm.setMinuteDelay(-5);
                break;
            case 2:
            case 3:
            case 4:
                alarm.setMinuteDelay(numberPickerDelayMinutes.getValue());
                break;

        }*/
        alarm.setTimeAlarm(TextUtils.getTimeWithDelayTime(textViewTime.getText().toString(),alarm.getMinuteDelay()));
        /**AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
         Intent intent = new Intent(this, AlarmReceiver.class);
         intent.putExtra(AlarmService.ALARM_ID_KEY,alarm.getId());
         PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
         // With setInexactRepeating(), you have to use one of the AlarmManager interval
         // constants--in this case, AlarmManager.INTERVAL_DAY.
         alarmManager.setRepeating(AlarmManager.RTC,
         HoursUtils.getTimeInCalendar(alarm.getTimeAlarm()).getTimeInMillis(),
         AlarmManager.INTERVAL_DAY,
         pendingIntent);*/
        Alarms.saveAlarmInManager(alarm,this);
    }

    private void removeAlarmManager(){
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent.setData(Uri.parse("custom://" + alarm.getId()));
        alarmIntent.setAction(String.valueOf(alarm.getId()));
        alarmIntent.putExtra(AlarmService.ALARM_ID_KEY,alarm.getId());
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent displayIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        alarmManager.cancel(displayIntent);
    }

    private void initViews(){
        initToolbar();
        initWeekDays();
        textViewTime = (TextView) findViewById(R.id.textViewTimeAlarm);
        editTextName = (EditText) findViewById(R.id.textViewAlarmName);
        switchActived = (SwitchCompat) findViewById(R.id.switchActived);
        /**numberPickerDelayMinutes = (NumberPicker) findViewById(R.id.numberPickerViewTime);
        numberPickerDelayMinutes.setMaxValue(59);
        numberPickerDelayMinutes.setMinValue(0);*/
        spinnerDelayType  = (Spinner) findViewById(R.id.spinnerDelayType);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.list_delay_type));
        spinnerDelayType.setAdapter(adapter);
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initWeekDays(){
        checkBoxSunday = (CheckBox) findViewById(R.id.checkboxSunday);
        checkBoxMonday = (CheckBox) findViewById(R.id.checkboxMonday);
        checkBoxTuesday = (CheckBox) findViewById(R.id.checkboxTuesday);
        checkBoxWednesday = (CheckBox) findViewById(R.id.checkboxWednesday);
        checkBoxThursday = (CheckBox) findViewById(R.id.checkboxThursday);
        checkBoxFriday = (CheckBox) findViewById(R.id.checkboxFriday);
        checkBoxSaturday = (CheckBox) findViewById(R.id.checkboxSaturday);
    }

    private void resumeWeekDays(){
        if(editing){
            checkBoxSunday.setChecked(alarm.isSunday());
            checkBoxMonday.setChecked(alarm.isMonday());
            checkBoxTuesday.setChecked(alarm.isTuesday());
            checkBoxWednesday.setChecked(alarm.isWednesday());
            checkBoxThursday.setChecked(alarm.isThursday());
            checkBoxFriday.setChecked(alarm.isFriday());
            checkBoxSaturday.setChecked(alarm.isSaturday());
        }
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
