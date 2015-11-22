package br.com.expressobits.hbus.ui;

/**
 * Created by Rafael on 09/06/2015.
 * A Activity que cont�m o fragment deve implementar esta interface
 */
public interface OnSettingsListener {

    /**
     * Escolhe atividade deve iniciar com hor�rios da linha e sentido selecionado
     * @param itinerary itinerario
     * @param way sentido
     */
    public void onSettingsDone(String itinerary,String way);

    /**
     *
     */
    public void onPopStackBack();


    /**
     * Notifica que foi removido favorito da lista de favoritos
     */
    public void onRemoveFavorite();

    /**
     * Notifica que foi adicionado itiner�rio na lista de favoritos
     */
    public void onAddFavorite();
}
