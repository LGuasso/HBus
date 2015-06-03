package br.com.expressobits.hbus.modelo;

import java.util.HashMap;

/**
 * Created by Rafael on 02/06/2015.
 */
public class Codigo {
    private String id;
    private String descricao;



    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Codigo){
            if(this.getId().equals(((Codigo)o).getId())){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getId();
    }
}
