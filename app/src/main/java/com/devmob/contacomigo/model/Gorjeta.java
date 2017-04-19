package com.devmob.contacomigo.model;

/**
 * Created by DevMachine on 12/04/2017.
 */

public class Gorjeta {


    private double valor;

    public int getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(int porcentagem) {
        this.porcentagem = porcentagem;
        valor =(double) porcentagem/100;
        valor = valor +1.0;

    }

    private int porcentagem;
    private Boolean ativo;

    public Gorjeta() {
        this.porcentagem = 10;
        this.valor = 1 + (this.porcentagem/100);
        this.ativo = false;
    }

    public double getValor() {
        return valor;
    }


    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
