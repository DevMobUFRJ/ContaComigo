package com.devmob.contacomigo.model;

/**
 * Created by silviomm on 05/04/17.
 */

public class Produto {

    private int id;

    private String nome;

    private double preco;


    public Produto() {

    }

    // TODO remove id from constructor, must be auto-incremented
    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }
}
