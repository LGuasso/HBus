package br.com.expressobits.hbus.ui.model;

/**
 * Simple model then look tip card on no have bookmark itineraries
 * @author Rafael Correa
 * @since 06/03/17
 */

public class HotTip {
    private int type;
    private String title;
    private String message;
    private int imageResource;
    private String buttonText;
    private String button2Text;


    public HotTip(int type,String title, String message, int imageResource, String buttonText) {
        setType(type);
        setTitle(title);
        setMessage(message);
        setImageResource(imageResource);
        setButtonText(buttonText);
    }

    public HotTip(int type,String title, String message, int imageResource, String buttonText, String button2Text) {
        this(type,title,message,imageResource,buttonText);
        setButton2Text(button2Text);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
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

    public String getButton2Text() {
        return button2Text;
    }

    public void setButton2Text(String button2Text) {
        this.button2Text = button2Text;
    }
}
