package com.devmob.contacomigo.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.model.Pessoa;

import java.util.ArrayList;
import java.util.List;

import static com.devmob.contacomigo.fragments.ItemFragmento.listAdapter;

public class AddPessoaActivity extends AppCompatActivity {

    private FloatingActionButton addFAB;
    private EditText nome;
    private Button botaoSalvar;
    private Button botaoCancelar;
    private List<EditText> pessoas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pessoa);

        botaoCancelar = (Button) findViewById(R.id.cancelar);
        botaoSalvar = (Button) findViewById(R.id.salvar);

        nome = (EditText) findViewById(R.id.nome);
        pessoas = new ArrayList<>();
        pessoas.add(nome);

        nome.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PessoaDAO dao = new PessoaDAO(AddPessoaActivity.this);
                for (EditText pessoa : pessoas) {
                    String nome = pessoa.getText().toString();
                    if (!nome.equals("")) {
                        dao.insere(new Pessoa(nome, 0));
                    }
                }
                finish();
            }
        });

        addFAB = (FloatingActionButton) findViewById(R.id.addFAB);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout myLayout = (LinearLayout) findViewById(R.id.layoutAddPessoa);
                EditText novaPessoa = new EditText(AddPessoaActivity.this);
                novaPessoa.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                );
                novaPessoa.setHint("Nova Pessoa");
                pessoas.add(novaPessoa);
                myLayout.addView(novaPessoa);
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    //  create a textWatcher member
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    void checkFieldsForEmptyValues() {
        String s1 = nome.getText().toString();

        if (s1.equals("")) {
            botaoSalvar.setEnabled(false);
        } else {
            botaoSalvar.setEnabled(true);
        }
    }

}
