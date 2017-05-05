package com.devmob.contacomigo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by silviomm on 12/04/17.
 */

public class PessoaProdutoDAO extends SQLiteOpenHelper {

    public PessoaProdutoDAO(Context context){
        super(context, "ContaComigo", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE PessoaProduto(" +
                "idPessoa INTEGER, " +
                "idProduto INTEGER," +
                "PRIMARY KEY(idPessoa, idProduto),"+
                "FOREIGN KEY(idPessoa) REFERENCES Pessoa(id),"+
                "FOREIGN KEY(idProduto) REFERENCES Produto(id)"+
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
