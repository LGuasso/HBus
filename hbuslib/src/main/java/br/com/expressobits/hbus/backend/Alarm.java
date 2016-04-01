package br.com.expressobits.hbus.backend;

import java.util.List;

/**
 * @author Rafael
 * @since 29/03/16
 * Class model of alarm
 */
public class Alarm {

    //cityID/itineraryID/Way/time
    private String id;
    //Days of week marked with this alarm
    private List<String> daysOfWeek;
    private String timeAlarm;
    /**
     * Minute delay,for delay positive is after <b>this alarm time</b> and negative for before
     */
    private int minuteDelay;
    /**
     * Name of alarm <i>Optional</i>
     */
    private String name;

    public String getId() {
        return id;
    }

    public int getMinuteDelay() {
        return minuteDelay;
    }

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public String getName() {
        return name;
    }

    public String getTimeAlarm() {
        return timeAlarm;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public void setMinuteDelay(int minuteDelay) {
        this.minuteDelay = minuteDelay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeAlarm(String time) {
        this.timeAlarm = time;
    }
}
