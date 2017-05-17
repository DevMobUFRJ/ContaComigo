package com.devmob.contacomigo.model;

/**
 * Created by silviomm on 05/04/17.
 */

public class Pessoa {

    private int id;

    private String nome;

    private double precoTotal;

    //TODO remove id from constructor, must be auto-incremented(sql)
    public Pessoa(int id, String nome, double precoTotal) {
        this.precoTotal = precoTotal;
        this.nome = nome;
        this.id = id;
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
