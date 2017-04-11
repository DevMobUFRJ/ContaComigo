package com.devmob.contacomigo.model;

/**
 * Created by silviomm on 05/04/17.
 */

public class Pessoa {

    private int id;

    private String nome;

    private double precoTotal;

    //TODO remove id from constructor, must be auto-incremented(sql)
    public Pessoa(String nome, int id) {
        this.nome = nome;
        this.id = id;
    }

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
}
