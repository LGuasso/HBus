package br.com.expressobits.hbus.modelo;

import java.util.ArrayList;

/**
 * Created by Rafael on 20/05/2015.
 */
public class Linha {

    private String nome;
    /**
     * Define o sentido da linha
     * <n>false</n> para bairro-centro
     * <n>true</n> para centro-bairro
     */
    private boolean sentido;
    /**
     * Define o dia da linha
     * @see TipoDeDia
     */
    private TipoDeDia dia;

    ArrayList<String> horarios;

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

    public TipoDeDia getDia() {
        return dia;
    }

    public void setDia(TipoDeDia dia) {
        this.dia = dia;
    }
}
