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
    private boolean sunday;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private String timeAlarm;
    /**
     * Minute delay,for delay positive is after <b>this alarm time</b> and negative for before
     */
    private int minuteDelay;
    /**
     * Name of alarm <i>Optional</i>
     */
    private String name;

    private boolean actived;

    private String code;

    public String getId() {
        return id;
    }

    public boolean isSunday() {
        return sunday;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public int getMinuteDelay() {
        return minuteDelay;
    }

    public String getName() {
        return name;
    }

    public String getTimeAlarm() {
        return timeAlarm;
    }

    public boolean isActived() {
        return actived;
    }

    public String getCode() {
        return code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
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

    public void setActived(boolean actived) {
        this.actived = actived;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
