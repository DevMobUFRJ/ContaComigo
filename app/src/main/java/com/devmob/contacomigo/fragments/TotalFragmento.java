package com.devmob.contacomigo.fragments;

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
import com.devmob.contacomigo.dao.ServicoDAO;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by devmob on 03/05/17.
 */

public class TotalFragmento extends Fragment implements FragmentInterface{


    private static final String TAG = "TotalFragmento";
    private String nomeFragmento = "Total";

    SectionedRecyclerViewAdapter sectionAdapter;


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
                Toast.makeText(getActivity(),  " AHHHHH ", Toast.LENGTH_SHORT).show();

            }
        });




        return view;
    }

    public String getNome(){
        return nomeFragmento;
    }

    @Override
    public void fragmentBecameVisible() {
        Log.d(TAG, "total frag interface");
    }
}
