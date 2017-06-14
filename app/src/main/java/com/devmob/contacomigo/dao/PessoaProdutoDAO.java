package com.devmob.contacomigo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;
import com.devmob.contacomigo.model.ProdutoConsumido;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by silviomm on 12/04/17.
 */

public class PessoaProdutoDAO extends DBAdapter {
    private static final String TAG = "PessoaProdutoDAO";


    public PessoaProdutoDAO(Context context) {
        super(context);
    }

    public void insere(int idPessoa, int idProduto, int quantidadeConsumida, float precoPago) {
        open();
        ContentValues dados = new ContentValues();
        dados.put("idPessoa", idPessoa);
        dados.put("idProduto", idProduto);
        dados.put("quantidadeConsumida", quantidadeConsumida);
        db.insert("PessoaProduto", null, dados);
        close();
    }

    //TODO RESOLVER QUERY
    public List<ProdutoConsumido> buscaProdutosDeUmaPessoa(Pessoa pessoa) {

        open();
        Cursor cursor = db.rawQuery("SELECT * FROM PessoaProduto WHERE idPessoa = ?",
                new String[]{"" + pessoa.getId()});
        List<Integer> ids = new ArrayList<>();
        List<ProdutoConsumido> l = new ArrayList<>();
        while (cursor.moveToNext()) {
            Integer id = new Integer(cursor.getInt(cursor.getColumnIndex("idProduto")));
            int quantidadeTemporaria = cursor.getInt(cursor.getColumnIndex("quantidadeConsumida"));
            float precoTemporario = cursor.getFloat(cursor.getColumnIndex("precoPago"));
            List<Float> lista = new ArrayList<>();
            lista.add(quantidadeTemporaria);
            lista.add(precoTemporario);
            l.add(id, lista);
            ids.add(id);

        }
        close();
        open();
        LinkedHashMap<Produto, List <Float> > produtos = new LinkedHashMap<>();
        for (Integer i : ids) {
            Cursor cursor2 = db.rawQuery("SELECT * FROM Produto WHERE id = ?", new String[]{"" + i});
            if (cursor2 != null)
                cursor2.moveToFirst();
            Produto produto = new Produto(
                    cursor2.getInt(cursor2.getColumnIndex("id")),
                    cursor2.getString(cursor2.getColumnIndex("nome")),
                    cursor2.getFloat(cursor2.getColumnIndex("preco")));

            produtos.put(produto,l.get(i));
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
                    cursor2.getString(cursor2.getColumnIndex("nome")));
            pessoas.add(pessoa);
        }
        close();
        return pessoas;
    }
}