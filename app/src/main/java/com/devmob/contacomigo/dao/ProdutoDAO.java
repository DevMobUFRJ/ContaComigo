package com.devmob.contacomigo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 12/04/17.
 */

public class ProdutoDAO extends SQLiteOpenHelper {

    public ProdutoDAO(Context context) {
        super(context, "ContaComigo", null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Produto(" +
                "id INTEGER PRIMARY KEY," +
                "nome VARCHAR(255) NOT NULL, " +
                "preco REAL NOT NULL" +
                ");";
        db.execSQL(sql);
    }


    public void insere(Produto produto){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();
        dados.put("nome", produto.getNome());
        dados.put("preco", produto.getPreco());

        db.insert("Produto", null, dados);
    }

    public List<Produto> buscaProdutos(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Produto;", null);
        List<Produto> produtos = new ArrayList<>();
        while(cursor.moveToNext()){
            Produto produto = new Produto(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getFloat(cursor.getColumnIndex("preco"))
            );
            produtos.add(produto);
        }
        return produtos;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Produto;";
        db.execSQL(sql);
        onCreate(db);
    }
}
