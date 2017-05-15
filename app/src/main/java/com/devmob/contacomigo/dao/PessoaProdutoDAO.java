package com.devmob.contacomigo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.PessoaProduto;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 12/04/17.
 */

public class PessoaProdutoDAO extends DBAdapter {

    public PessoaProdutoDAO(Context context){
        super(context);
    }

    public void insere(int idPessoa, int idProduto){
        open();
        ContentValues dados = new ContentValues();
        dados.put("idPessoa", idPessoa);
        dados.put("idProduto", idProduto);

        db.insert("PessoaProduto", null, dados);
        close();
    }

    public List<PessoaProduto> buscaPessoaProduto(){
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM PessoaProduto;", null);
        List<PessoaProduto> listpepo = new ArrayList<>();
        while(cursor.moveToNext()){
            PessoaProduto pepo = new PessoaProduto(
                    cursor.getInt(cursor.getColumnIndex("idPessoa")),
                    cursor.getInt(cursor.getColumnIndex("idProduto")));
            listpepo.add(pepo);
        }
        close();
        return listpepo;
    }
}