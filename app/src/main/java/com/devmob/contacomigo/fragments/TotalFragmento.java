package com.devmob.contacomigo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.activities.MainActivity;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.dao.ServicoDAO;
import com.devmob.contacomigo.model.Pessoa;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by devmob on 03/05/17.
 */

public class TotalFragmento extends Fragment implements FragmentInterface{


    private static final String TAG = "TotalFragmento";
    private String nomeFragmento = "Total";
    private boolean atualizar = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.total_layout, container, false);
        Button apagaTudo = (Button) view.findViewById(R.id.deletaTudo);
        apagaTudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServicoDAO dao = new ServicoDAO(getContext());
                dao.deletaBanco();
                PessoaDAO PessoaDao = new PessoaDAO(getContext());
                Pessoa p = new Pessoa(getResources().getString(R.string.default_person));
                PessoaDao.insere(p);
                Toast.makeText(getActivity(),  "Já pode começar outra conta ;)", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor prefEditor = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();
                prefEditor.clear();
                prefEditor.commit();
                Intent i = getContext().getPackageManager()
                        .getLaunchIntentForPackage( getContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(i);
                resetaGorjeta();
                MainActivity.pessoaFragmento.setAtualizar(true);
                MainActivity.itemFragmento.setAtualizar(true);

            }
        });




        return view;
    }

    private void resetaGorjeta() {
        ItemFragmento.gorjeta.setAtivo(false);
        PessoaFragmento.gorjeta.setAtivo(false);
        ItemFragmento.gorjeta.setPorcentagem(10);
        PessoaFragmento.gorjeta.setPorcentagem(10);
        ItemFragmento.gorjetaSpinner.setSelection(ItemFragmento.gorjeta.getPorcentagem()-1);
        PessoaFragmento.gorjetaSpinner.setSelection(PessoaFragmento.gorjeta.getPorcentagem()-1);
        ItemFragmento.switchGorjeta.setChecked(false);
        PessoaFragmento.switchGorjeta.setChecked(false);
    }

    public String getNome(){
        return nomeFragmento;
    }

    @Override
    public void setAtualizar(boolean b) {
        this.atualizar = b;
    }

    @Override
    public void fragmentBecameVisible() {
        if(atualizar){
            this.atualizar = false;
            Log.i(TAG, "TotalFrag atualizada");
        }
    }
}
