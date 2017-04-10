package com.devmob.contacomigo.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by silviomm on 10/04/17.
 */

public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "contacomigo";
    private static final int VERSAO = 1;
    private static final String PRODUTOS = "produto";

    private static final String ID = "_id";
    private static final String NOME = "nome";
    private static final String PRECO = "preco";

    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE"+PRODUTOS+"("
                + ID + "integer primary ket autoincrement"
                + NOME + "text, "
                + PRECO + "float;";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
