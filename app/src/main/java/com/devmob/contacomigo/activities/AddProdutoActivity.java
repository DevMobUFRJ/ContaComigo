package com.devmob.contacomigo.activities;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.fragments.ItemFragmento;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class AddProdutoActivity extends AppCompatActivity {
    public EditText nomeT;
    public EditText precoT;
    public Button botaoSalvar;
    public Button botaoCancelar;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);
        intent = getIntent();
        nomeT = (EditText) findViewById(R.id.nome);
        precoT = (EditText) findViewById(R.id.preco);
        botaoSalvar = (Button) findViewById(R.id.salvar);
        botaoCancelar = (Button) findViewById(R.id.cancelar);
        nomeT.addTextChangedListener(mTextWatcher);
        precoT.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeT.getText().toString();
                float preco = Float.parseFloat(precoT.getText().toString());
                Produto produto = new Produto(nome, preco);

                ProdutoDAO dao = new ProdutoDAO(AddProdutoActivity.this);
                dao.insere(produto);
                dao.close();
                ItemFragmento.listAdapter.updateLista(produto);
                Toast.makeText(AddProdutoActivity.this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();

                intent.putExtra("booleanItem", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("booleanItem", false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        PessoaDAO pessoaDAO = new PessoaDAO(AddProdutoActivity.this);
        List<Pessoa> pessoas = pessoaDAO.buscaPessoas();

        String[] array = new String[]{"Apple", "Google"};
        ViewGroup checkboxContainer = (ViewGroup) findViewById(R.id.checkbox_container);
        for (Pessoa pessoa : pessoas){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(pessoa.getNome());
            checkboxContainer.addView(checkBox);
        }
        for (int i = 0; i < array.length; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(array[i]);
            checkboxContainer.addView(checkBox);
        }
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

    void checkFieldsForEmptyValues(){
        String s1 = nomeT.getText().toString();
        String s2 = precoT.getText().toString();

        if(s1.equals("")|| s2.equals("")){
            botaoSalvar.setEnabled(false);
        } else {
            botaoSalvar.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        intent.putExtra("booleanItem", false);
        setResult(RESULT_OK, intent);
        finish();
    }
}
