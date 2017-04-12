package com.devmob.contacomigo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by silviomm on 12/04/17.
 */

public class PessoaDAO extends SQLiteOpenHelper {

    public PessoaDAO(Context context){
        super(context, "ContaComigo", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Pessoa(" +
                "id INTEGER PRIMARY KEY, " +
                "nome VARCHAR(255) NOT NULL, " +
                "precoTotal REAL NOT NULL" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
