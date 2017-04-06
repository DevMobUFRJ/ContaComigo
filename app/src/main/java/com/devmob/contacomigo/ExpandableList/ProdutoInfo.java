package com.devmob.contacomigo.ExpandableList;

import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;

/**
 * Created by DevMachine on 30/03/2017.
 */


public class ProdutoInfo {

    Produto produto;

    private ArrayList<PessoaInfo> list = new ArrayList<>();

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getNomeProduto() {
        return produto.getNome();
    }

    public double getProdutoPreco() {
        return produto.getPreco();
    }

    public void setNomeProduto(String nome) {
        produto.setNome(nome);
    }

    public ArrayList<PessoaInfo> getListProduto() {
        return list;
    }

    public void setListPessoa(ArrayList<PessoaInfo> pessoa) {
        this.list = pessoa;
    }

}