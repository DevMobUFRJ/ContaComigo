package com.devmob.contacomigo.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmob.contacomigo.R;

/**
 * Created by devmob on 03/05/17.
 */

public class PessoaFragmento extends Fragment {
    private static final String TAG = "PessoaFragmento";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.pessoas_layout, container, false);

        return view;
    }
}
