package com.devmob.contacomigo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.PessoaProduto;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 12/04/17.
 */

public class PessoaProdutoDAO extends DBAdapter {
    private static final String TAG = "PessoaProdutoDAO";


    public PessoaProdutoDAO(Context context) {
        super(context);
    }

    public void insere(int idPessoa, int idProduto) {
        open();
        ContentValues dados = new ContentValues();
        dados.put("idPessoa", idPessoa);
        dados.put("idProduto", idProduto);
        db.insert("PessoaProduto", null, dados);
        close();
    }

    public List<Produto> buscaProdutosDeUmaPessoa(Pessoa pessoa) {
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM PessoaProduto WHERE idPessoa = ?",
                new String[]{"" + pessoa.getId()});
        List<Integer> ids = new ArrayList<>();
        while (cursor.moveToNext()) {
            Integer id = new Integer(cursor.getInt(cursor.getColumnIndex("idProduto")));
            ids.add(id);
        }
        close();
        open();
        List<Produto> produtos = new ArrayList<>();
        for (Integer i : ids) {
            Cursor cursor2 = db.rawQuery("SELECT * FROM Produto WHERE id = ?", new String[]{"" + i});
            if (cursor2 != null)
                cursor2.moveToFirst();
            Produto produto = new Produto(
                    cursor2.getInt(cursor2.getColumnIndex("id")),
                    cursor2.getString(cursor2.getColumnIndex("nome")),
                    cursor2.getFloat(cursor2.getColumnIndex("preco")));
            produtos.add(produto);
        }
        close();
        return produtos;
    }

    public List<Pessoa> buscaPessoasDeUmProduto(Produto produto) {
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM PessoaProduto WHERE idProduto = ?",
                new String[]{"" + produto.getId()});
        List<Integer> ids = new ArrayList<>();
        while (cursor.moveToNext()) {
            Integer id = new Integer(cursor.getInt(cursor.getColumnIndex("idPessoa")));
            ids.add(id);
        }
        close();
        open();
        List<Pessoa> pessoas = new ArrayList<>();
        for (Integer i : ids) {
            Cursor cursor2 = db.rawQuery("SELECT * FROM Pessoa WHERE id = ?", new String[]{"" + i});
            if (cursor2 != null)
                cursor2.moveToFirst();
            Pessoa pessoa = new Pessoa(
                    cursor2.getInt(cursor2.getColumnIndex("id")),
                    cursor2.getString(cursor2.getColumnIndex("nome")),
                    cursor2.getFloat(cursor2.getColumnIndex("precoTotal")));
            pessoas.add(pessoa);
        }
        close();
        return pessoas;
    }
}