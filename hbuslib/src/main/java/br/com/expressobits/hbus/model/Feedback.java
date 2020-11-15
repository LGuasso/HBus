package br.com.expressobits.hbus.model;

import java.util.List;

/**
 * @author Rafael
 * @since 14/01/16.
 */
public class Feedback {
    private Long id;
    private String message;
    private String email;
    private int type;
    private List<String> systemInformation;

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public int getType() {
        return type;
    }

    public List<String> getSystemInformation() {
        return systemInformation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSystemInformation(List<String> systemInformation) {
        this.systemInformation = systemInformation;
    }
}
