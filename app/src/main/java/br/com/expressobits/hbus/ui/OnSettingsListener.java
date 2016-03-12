package br.com.expressobits.hbus.ui;

/**
 * Created by Rafael on 09/06/2015.
 * A Activity que cont�m o fragment deve implementar esta interface
 */
public interface OnSettingsListener {

    /**
     * Escolhe atividade deve iniciar com hor�rios da itinerary e way selecionado
     * @param itineraryId itinerario
     * @param way way
     */
    public void onSettingsDone(String itineraryId,String way);

    /**
     * Escolhe atividade deve iniciar com hor�rios da itinerary e way selecionado
     */
    public void addFragment(String TAG);

}