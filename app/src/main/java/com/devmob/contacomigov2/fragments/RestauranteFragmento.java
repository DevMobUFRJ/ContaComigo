package com.devmob.contacomigov2.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmob.contacomigov2.R;
import com.devmob.contacomigov2.interfaces.FragmentInterface;

/**
 * Created by devmob on 03/05/17.
 */

public class RestauranteFragmento extends Fragment implements FragmentInterface {
    private static final String TAG = "RestauranteFragmento";
    private boolean atualizar = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view  = inflater.inflate(R.layout.restaurante_layout, container, false);
        return view;
    }

    @Override
    public void setAtualizar(boolean b) {
        this.atualizar = b;
    }

    public void forceReload(){
        this.atualizar = true;
        fragmentBecameVisible();
    }

    @Override
    public void fragmentBecameVisible() {
        if(atualizar){
            Log.d(TAG, "restaurante frag interface");
        }
    }
}
