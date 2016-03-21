package br.com.expressobits.hbus.backend;

import java.util.List;

/**
 * Modelo de um feedback enviado para GCDE através de endpoints
 * @author Rafael Correa
 * @since 10/03/16
 */
public class Feedback {

    private String message;
    private String email;
    private int type;
    private List<String> informationSystem;

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getInformationSystem() {
        return informationSystem;
    }

    public String getEmail() {
        return email;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInformationSystem(List<String> informationSystem) {
        this.informationSystem = informationSystem;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
