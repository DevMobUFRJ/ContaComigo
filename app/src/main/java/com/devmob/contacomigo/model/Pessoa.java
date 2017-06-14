package com.devmob.contacomigo.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by silviomm on 05/04/17.
 */

public class Pessoa {

    private int id;

    private String nome;

    private List<ProdutoConsumido> produtosConsumidos = new ArrayList<>();

    //TODO remove id from constructor, must be auto-incremented(sql)
    public Pessoa(int id, String nome) {
        this.nome = nome;
        this.id = id;
    }

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public float getPrecoTotal(){
        float precoTotal = 0;
        for (ProdutoConsumido produtoC: produtosConsumidos) {
            precoTotal += produtoC.getPrecoPago();
        }
        return precoTotal;
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

}
