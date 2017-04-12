package com.devmob.contacomigo.model;

/**
 * Created by silviomm on 05/04/17.
 */

public class Produto {

    private int id;

    private String nome;

    private float preco;

    public Produto(String nome, float preco){
        this.nome = nome;
        this.preco = preco;
    }

    public Produto(int id, String nome, float preco) {
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
