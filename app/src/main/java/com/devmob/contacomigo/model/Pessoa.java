package com.devmob.contacomigo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by silviomm on 05/04/17.
 */

public class Pessoa implements Serializable{

    private int id;

    private String nome;

    private List<ProdutoConsumido> produtosConsumidos = new ArrayList<>();

    private float precoTotal;

    //TODO remove id from constructor, must be auto-incremented(sql)
    public Pessoa(int id, String nome) {
        this.nome = nome;
        this.id = id;
    }

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public float getPrecoTotal(){
        return this.precoTotal;
    }

    public List<ProdutoConsumido> getProdutosConsumidos() {

        return produtosConsumidos;
    }

    public void setProdutosConsumidos(List<ProdutoConsumido> produtosConsumidos) {
        this.produtosConsumidos = produtosConsumidos;
    }


    public int getId() { return id; }

    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(int id){ this.id = id; }

    public void setPrecoTotal(float precoTotal) {
        this.precoTotal = precoTotal;
    }
}
