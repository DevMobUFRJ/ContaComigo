package com.devmob.contacomigov2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.devmob.contacomigov2.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 12/04/17.
 */

public class ProdutoDAO extends DBAdapter{

    private static final String TAG = "ProdutoDAO";

    public ProdutoDAO(Context context) {
        super(context);
    }


    public void insere(Produto produto){
        open();
        ContentValues dados = new ContentValues();
        dados.put("nome", produto.getNome());
        dados.put("preco", produto.getPreco());
        dados.put("quantidade", produto.getQuantidade());

        produto.setId((int)db.insert("Produto", null, dados));

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
                    cursor.getFloat(cursor.getColumnIndex("preco")),
                    cursor.getInt(cursor.getColumnIndex("quantidade"))
            );
            produtos.add(produto);
        }
        PessoaProdutoDAO ppdao = new PessoaProdutoDAO(mCtx);
        for(Produto produto : produtos){
            produto.setConsumidores(ppdao.buscaPessoasDeUmProduto(produto));
        }
        close();
        return produtos;
    }

    public void editaProduto(Produto produto){
        open();
        ContentValues cv = new ContentValues();
        cv.put("nome", produto.getNome());
        cv.put("preco", produto.getPreco());
        cv.put("quantidade", produto.getQuantidade());
        db.update("Produto",cv, "id="+produto.getId(), null);
        close();
    }

    public Produto getProdutoById(int id){
        open();
        Produto produto = null;
        Cursor cursor = db.rawQuery("SELECT * FROM Produto WHERE id = "+id+";", null);
        if(cursor!=null) {
            if (cursor.moveToFirst()) {
                produto = new Produto(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("nome")),
                        cursor.getFloat(cursor.getColumnIndex("preco")),
                        cursor.getInt(cursor.getColumnIndex("quantidade"))
                );
            }
        }
        if(produto!=null){
            PessoaProdutoDAO ppdao = new PessoaProdutoDAO(mCtx);
            produto.setConsumidores(ppdao.buscaPessoasDeUmProduto(produto));
            close();
            return produto;
        }
        close();
        return null;
    }

    public void deletaProduto(Produto produto) {
        open();
        db.execSQL("DELETE FROM Produto WHERE id = ?;", new String[]{"" + produto.getId()});
        close();
    }

    public void deletaRelacao(Produto produto) {
        open();
        db.execSQL("DELETE FROM PessoaProduto WHERE idProduto = ?;", new String[]{"" + produto.getId()});
        close();
    }
}
