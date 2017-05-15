package com.devmob.contacomigo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devmob on 15/05/17.
 */

public class PessoaProduto {

       private int idPessoa;
       private int idProduto;


    public PessoaProduto(int idPessoa, int idProduto) {
        this.idProduto = idProduto;
        this.idPessoa = idPessoa;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }
}
