package com.devmob.contacomigo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.devmob.contacomigo.model.Produto;

/**
 * Created by silviomm on 10/04/17.
 */

public class ProdutoDAO {

    private SQLiteDatabase bd;
    private CriaBanco banco;

    public ProdutoDAO(Context context) {
        banco = new CriaBanco(context);
    }

    public void insere(Produto produto){
        bd = banco.getWritableDatabase();
        String query = "INSERT INTO produto VALUES ("
                + produto.getNome() + ","
                + produto.getPreco() + ");";
        bd.execSQL(query);
    }

}
