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

    private double precoTotal;

    private LinkedHashMap<Produto, LinkedHashMap<Float, Float>> produtos = new LinkedHashMap<>();

    public List<Produto> getProdutosNomes() {
        List<Produto> prod = new ArrayList<>(produtos.keySet());
        return prod;
    }

    public void setProdutos(LinkedHashMap<Produto, LinkedHashMap<Float, Float>> produtos) {
        this.produtos = produtos;
    }

    //TODO remove id from constructor, must be auto-incremented(sql)
    public Pessoa(int id, String nome, double precoTotal) {
        this.precoTotal = precoTotal;
        this.nome = nome;
        this.id = id;
    }

    public Pessoa(String nome, double precoTotal) {
        this.precoTotal = precoTotal;
        this.nome = nome;
    }

    public int getId() { return id; }

    public String getNome() {
        return nome;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(int id){ this.id = id; }

}
