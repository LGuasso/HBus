package br.com.expressobits.hbus.ui.model;

/**
 * @author Rafael Correa
 * @since 21/02/17
 */

public class Header {

    private String textHeader;

    public Header(String textHeader) {
        this.textHeader = textHeader;
    }

    public String getTextHeader() {
        return textHeader;
    }

    public void setTextHeader(String textHeader) {
        this.textHeader = textHeader;
    }
}
