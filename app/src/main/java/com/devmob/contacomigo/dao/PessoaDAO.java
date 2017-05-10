package com.devmob.contacomigo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 12/04/17.
 */

public class PessoaDAO extends SQLiteOpenHelper {

    public PessoaDAO(Context context){
        super(context, "ContaComigo", null, 67);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Pessoa(" +
                "id INTEGER PRIMARY KEY, " +
                "nome VARCHAR(255) NOT NULL, " +
                "precoTotal REAL NOT NULL " +
                ");";
        db.execSQL(sql);
    }

    public List<Pessoa> buscaPessoas(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Pessoa;", null);
        List<Pessoa> pessoas = new ArrayList<>();
        while(cursor.moveToNext()){
            Pessoa pessoa = new Pessoa(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getFloat(cursor.getColumnIndex("precoTotal")));
            pessoas.add(pessoa);
        }
        return pessoas;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Pessoa;";
        db.execSQL(sql);
        onCreate(db);
    }
}
