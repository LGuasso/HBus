package br.com.expressobits.hbus.ui;

/**
 * Created by Rafael on 09/06/2015.
 * A Activity que cont�m o fragment deve implementar esta interface
 */
public interface OnSettingsListener {

    /**
     * Escolhe atividade deve iniciar com hor�rios da itinerary e way selecionado
     * @param itinerary itinerario
     * @param way way
     */
    public void onSettingsDone(String company,String itinerary,String way);

    /**
     * Escolhe atividade deve iniciar com hor�rios da itinerary e way selecionado
     */
    public void addFragment(String TAG);

}
