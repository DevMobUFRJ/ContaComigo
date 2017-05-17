package com.devmob.contacomigo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 05/04/17.
 */

public class Produto {

    private int id;

    private String nome;

    private float preco;

    private List<Pessoa> consumidores = new ArrayList<>();

    public List<Pessoa> getConsumidores(){
        return this.consumidores;
    }

    public void setConsumidores(List<Pessoa> consumidores){
        this.consumidores = consumidores;
    }

    public Produto(String nome, float preco){
        this.nome = nome;
        this.preco = preco;
    }

    public Produto(int id, String nome, float preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
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

    public int getId() {
        return id;
    }
}
