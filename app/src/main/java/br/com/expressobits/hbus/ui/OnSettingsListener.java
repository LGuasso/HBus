package br.com.expressobits.hbus.ui;

/**
 * Created by Rafael on 09/06/2015.
 * A Activity que contém o fragment deve implementar esta interface
 */
public interface OnSettingsListener {

    /**
     * Escolhe atividade deve iniciar com horários da linha e sentido selecionado
     * @param linha
     * @param sentido
     */
    public void onSettingsDone(String linha,String sentido);


    /**
     * Escolhe atividade de favoritos deve ser selecionada
     * @param type
     */
    public void onSettingsDone(boolean type);


    /**
     * Notifica que foi removido favorito da lista de favoritos
     */
    public void onRemoveFavorite();

    /**
     * Notifica que foi adicionado itinerário na lista de favoritos
     */
    public void onAddFavorite();
}
