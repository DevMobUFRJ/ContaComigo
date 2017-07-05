package com.devmob.contacomigo.dao;

import android.content.Context;

/**
 * Created by DevMob on 05/07/2017.
 */

public class ServicoDAO extends DBAdapter {
    private static final String TAG = "ServicoDAO";


    public ServicoDAO(Context context){
        super(context);
    }

    public void deletaBanco(){
        open();
        db.execSQL("DELETE FROM Pessoa;");
        db.execSQL("DELETE FROM Produto;");
        db.execSQL("DELETE FROM PessoaProduto;");
    }

}
