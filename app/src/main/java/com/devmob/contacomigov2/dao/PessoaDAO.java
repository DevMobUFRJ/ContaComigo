package com.devmob.contacomigov2.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.devmob.contacomigov2.model.Pessoa;
import com.devmob.contacomigov2.model.ProdutoConsumido;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silviomm on 12/04/17.
 */

public class PessoaDAO extends DBAdapter {
    private static final String TAG = "PessoaDAO";


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
            float soma = 0;
            for(ProdutoConsumido pc : pessoa.getProdutosConsumidos()){
                soma += pc.getPrecoPago();
            }
            pessoa.setPrecoTotal(soma);
        }
        close();
        return pessoas;
    }
    public Pessoa getPessoaById(int id){
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM Pessoa;", null);

        while(cursor.moveToNext()){
            Pessoa pessoa = new Pessoa(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")));
            if (pessoa.getId() == id){
                PessoaProdutoDAO ppdao = new PessoaProdutoDAO(mCtx);
                pessoa.setProdutosConsumidos(ppdao.buscaProdutosDeUmaPessoa(pessoa));
                float soma = 0;
                for(ProdutoConsumido pc : pessoa.getProdutosConsumidos()){
                    soma += pc.getPrecoPago();
                }
                pessoa.setPrecoTotal(soma);
                return pessoa;
            }
        }
        close();
        return null;
    }
    public void deletaPessoa(Pessoa pessoa) {
        open();
        db.execSQL("DELETE FROM Pessoa WHERE id = ?;", new String[]{"" + pessoa.getId()});
        close();
    }

    public void deletaRelacao(Pessoa pessoa) {
        open();
        db.execSQL("DELETE FROM PessoaProduto WHERE idPessoa = ?;", new String[]{"" + pessoa.getId()});
        close();
    }

    public boolean isEmpty(){
        open();
        Cursor cursor = db.rawQuery("SELECT count(*) FROM Pessoa;", null);
        cursor.moveToFirst();
        int icount = cursor.getInt(0);
        close();
        if(icount>0)
            return false;
        else
            return true;
    }
}
