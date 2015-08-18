package br.com.expressobits.hbus.model;

import java.util.ArrayList;
import java.util.Collections;

import br.com.expressobits.hbus.utils.TimeUtils;

/**
 * Created by Rafael on 20/05/2015.
 * @deprecated
 */
public class Line {

    private int id;

    private String nome;
    /**
     * Define o sentido da linha
     * <n>false</n> para bairro-centro
     * <n>true</n> para centro-bairro
     */
    private boolean sentido;
    /**
     * Define o dia da linha
     * @see TypeDay
     */
    private TypeDay dia;


    ArrayList<Bus> onibuses;


    public void setOnibuses(ArrayList<Bus> onibuses) {
        this.onibuses = onibuses;
    }

    public ArrayList<Bus> getOnibuses() {
        return onibuses;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isSentido() {
        return sentido;
    }

    public void setSentido(boolean sentido) {
        this.sentido = sentido;
    }

    public TypeDay getDia() {
        return dia;
    }

    public void setDia(TypeDay dia) {
        this.dia = dia;
    }






}
