package com.devmob.contacomigo.dao;

import android.content.ContentValues;
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

public class PessoaDAO extends DBAdapter {

    public PessoaDAO(Context context){
        super(context);
    }

    public void insere(Pessoa pessoa){
        open();
        ContentValues dados = new ContentValues();
        dados.put("nome", pessoa.getNome());
        dados.put("precoTotal", pessoa.getPrecoTotal());

        pessoa.setId((int)db.insert("Pessoa", null, dados));
        close();
    }

    public List<Pessoa> buscaPessoas(){
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM Pessoa;", null);
        List<Pessoa> pessoas = new ArrayList<>();
        while(cursor.moveToNext()){
            Pessoa pessoa = new Pessoa(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")));
            pessoas.add(pessoa);
        }
        PessoaProdutoDAO ppdao = new PessoaProdutoDAO(mCtx);
        for(Pessoa pessoa : pessoas){
            pessoa.setProdutosConsumidos(ppdao.buscaProdutosDeUmaPessoa(pessoa));
        }
        close();
        return pessoas;
    }

}
