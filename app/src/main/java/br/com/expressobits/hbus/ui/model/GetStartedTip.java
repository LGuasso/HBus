package br.com.expressobits.hbus.ui.model;

/**
 * Simple model then look tip card on no have bookmark itineraries
 * @author Rafael Correa
 * @since 06/03/17
 */

public class GetStartedTip {
    private String title;
    private String message;
    private int imageResource;
    private String buttonText;

    public GetStartedTip(String title, String message, int imageResource, String buttonText) {
        this.title = title;
        this.message = message;
        this.imageResource = imageResource;
        this.buttonText = buttonText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
