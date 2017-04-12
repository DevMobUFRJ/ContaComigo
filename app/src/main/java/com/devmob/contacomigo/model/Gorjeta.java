package com.devmob.contacomigo.model;

/**
 * Created by DevMachine on 12/04/2017.
 */

public class Gorjeta {

    private double valor;
    private Boolean ativo;

    public Gorjeta() {
        this.valor = 1.1;
        this.ativo = false;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
