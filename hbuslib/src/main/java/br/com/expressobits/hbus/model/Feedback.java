package br.com.expressobits.hbus.model;

/**
 * @author Rafael
 * @since 14/01/16.
 */
public class Feedback {
    private Long id;
    private String text;
    private String type;
    private String systemInformation;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String getSystemInformation() {
        return systemInformation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSystemInformation(String systemInformation) {
        this.systemInformation = systemInformation;
    }
}
