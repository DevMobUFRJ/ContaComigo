package com.devmob.contacomigo.fragments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.activities.AddPessoaActivity;
import com.devmob.contacomigo.activities.AddProdutoActivity;

import static com.devmob.contacomigo.fragments.ItemFragmento.listAdapter;

/**
 * Created by devmob on 03/05/17.
 */

public class PessoaFragmento extends Fragment {
    private static final String TAG = "PessoaFragmento";


    public FloatingActionButton addFAB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.pessoas_layout, container, false);


        addFAB = (FloatingActionButton) view.findViewById(R.id.addFAB);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
                telaAdicionar();
                listAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void telaAdicionar() {
        Intent intent = new Intent(getActivity(), AddPessoaActivity.class);
        //intent.putExtra(getString(R.string.key_name), name);
        startActivityForResult(intent, 1);
    }
}
