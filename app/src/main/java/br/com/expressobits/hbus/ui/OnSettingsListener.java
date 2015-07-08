package br.com.expressobits.hbus.ui;

/**
 * Created by Rafael on 09/06/2015.
 * A Activity que contém o fragment deve implementar esta interface
 */
public interface OnSettingsListener {
    /**
     * TODO fazer DOC
     * @param linha
     * @param sentido
     */
    public void onSettingsDone(String linha,String sentido);


    /**
     * TODO fazer DOC
     * @param type
     */
    public void onSettingsDone(boolean type);
}
