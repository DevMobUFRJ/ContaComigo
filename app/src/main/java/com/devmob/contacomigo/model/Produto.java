package com.devmob.contacomigo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 05/04/17.
 */

public class Produto implements Serializable{

    private int id;

    private String nome;

    private float preco;

    private int quantidade;



    private List<Pessoa> consumidores = new ArrayList<>();

    public List<Pessoa> getConsumidores(){
        return this.consumidores;
    }

    public void setConsumidores(List<Pessoa> consumidores){
        this.consumidores = consumidores;
    }

    public Produto(String nome, float preco, int quantidade){
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Produto(int id, String nome, float preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public void setId(int id){
        this.id = id;
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

    public void setPreco(float preco){this.preco = preco;}

    public int getId() {
        return id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
