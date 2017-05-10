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

public class TotalFragmento extends Fragment {
    private static final String TAG = "TotalFragmento";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.total_layout, container, false);

        return view;
    }
}
