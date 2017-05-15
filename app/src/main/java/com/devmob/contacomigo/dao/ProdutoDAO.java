package com.devmob.contacomigo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 12/04/17.
 */

public class ProdutoDAO extends DBAdapter{

    public ProdutoDAO(Context context) {
        super(context);
    }


    public void insere(Produto produto){
        open();
        ContentValues dados = new ContentValues();
        dados.put("nome", produto.getNome());
        dados.put("preco", produto.getPreco());

        db.insert("Produto", null, dados);
        close();
    }

    public List<Produto> buscaProdutos(){
        open();
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
        close();
        return produtos;
    }


}
