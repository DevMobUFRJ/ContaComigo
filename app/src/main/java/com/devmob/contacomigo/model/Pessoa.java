package com.devmob.contacomigo.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by silviomm on 05/04/17.
 */

@Table(name = "Pessoa")
public class Pessoa extends Model {

    private int id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "precoTotal")
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
