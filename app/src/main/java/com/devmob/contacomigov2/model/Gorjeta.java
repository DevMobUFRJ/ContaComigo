package com.devmob.contacomigov2.model;

/**
 * Created by DevMachine on 12/04/2017.
 */

public class Gorjeta {


    private double valor;

    private int porcentagem;
    private Boolean ativo;

    public int getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(int porcentagem) {
        this.porcentagem = porcentagem;
        valor =(double) porcentagem/100;
        valor = valor +1.0;

    }

    public Gorjeta(int porcentagem, boolean ativo) {
        this.porcentagem = porcentagem;
        this.valor =(double) porcentagem/100;
        this.valor = this.valor +1.0;
        this.ativo = ativo;
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
