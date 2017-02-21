package br.com.expressobits.hbus.ui;

/**
 * Interface implement selections schedule for activity times
 * @author Rafael Correa
 * @since 09/06/2015
 */
public interface OnSettingsListener {

    /**
     * Selection company ,itinerary and way
     * @param itinerary itinerario
     * @param way way
     */
    void onSettingsDone(String company,String itinerary,String way);

    /**
     * Add fragment tag
     */
    void addFragment(String TAG);

}
