package com.devmob.contacomigo.ExpandableList;

import com.devmob.contacomigo.model.Pessoa;


/**
 * Created by DevMachine on 30/03/2017.
 */

public class PessoaInfo {

    private Pessoa pessoa;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setNomePessoa(String nome){
        this.pessoa.setNome(nome);
    }

    public String getNomePessoa(){
        return pessoa.getNome();
    }

    public double getPreco() {
        return pessoa.getPrecoTotal();
    }

    public void setPreco(double preco) {
        pessoa.setPrecoTotal(preco);
    }

}