package com.devmob.contacomigo.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by silviomm on 05/04/17.
 */

@Table(name = "produto")
public class Produto extends Model {

    private int id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "preco")
    private double preco;


    public Produto(){
        super();
    }

    // TODO remove id from constructor, must be auto-incremented
    public Produto(int id, String nome, double preco) {
        super();
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
