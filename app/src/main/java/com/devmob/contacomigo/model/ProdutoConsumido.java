package com.devmob.contacomigo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devmob on 09/06/17.
 */

public class ProdutoConsumido {

    private Produto produto;
    private int quantidade;
    private float precoPago;

    public ProdutoConsumido(Produto produto, int quantidade, float precoPago) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoPago = precoPago;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getPrecoPago() {
        return precoPago;
    }

    public void setPrecoPago(float precoPago) {
        this.precoPago = precoPago;
    }
}