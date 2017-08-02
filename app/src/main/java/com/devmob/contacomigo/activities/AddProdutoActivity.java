package com.devmob.contacomigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.dao.PessoaProdutoDAO;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

import me.himanshusoni.quantityview.QuantityView;

public class AddProdutoActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener{

    private static final String TAG = "AddProdutoActivity";

    public EditText nomeT;
    public EditText precoT;
    public Button botaoSalvar;
    public Button botaoCancelar;
    ViewGroup checkboxOuterContainer;
    QuantityView quantityView;

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
        quantityView = (QuantityView) findViewById(R.id.quantityView);
        nomeT.addTextChangedListener(mTextWatcher);
        precoT.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
        checkboxOuterContainer = (ViewGroup) findViewById(R.id.checkbox_outer_container);
        quantityView.setOnQuantityChangeListener(this);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProdutoDAO produtodao = new ProdutoDAO(AddProdutoActivity.this);
                PessoaProdutoDAO ppd = new PessoaProdutoDAO(AddProdutoActivity.this);
                String nome = nomeT.getText().toString();
                float preco = Float.parseFloat(precoT.getText().toString());
                int quantidadeProdutos = quantityView.getQuantity();
                Log.d(TAG, "onClick: aqui" + quantidadeProdutos);
                Produto produto = new Produto(nome, preco, quantidadeProdutos);
                produtodao.insere(produto);
                produtodao.close();
                Toast.makeText(AddProdutoActivity.this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
                List<Integer> idsConsumidores = new ArrayList<Integer>();
                //TODO getQuantityPerPerson
                //QuantitiView está em View v = ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                //O que muda: precoPorPessoa tera calculo mais complexo
                //"quantidadeConsumida" vai passar o que pega acima
                
                for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                    View v = ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                    if (v instanceof CheckBox) {
                        if (((CheckBox) v).isChecked()) {
                            idsConsumidores.add(v.getId());
                        }
                    }
                }
                float precoPorPessoa = (float)(produto.getPreco()/idsConsumidores.size());
                //temporário
                //TODO PEGAR DA TELA DE ADICIONAR
                int quantidadeConsumida = 1;
                for (Integer id : idsConsumidores) {
                    ppd.insere(id, produto.getId(), quantidadeConsumida, precoPorPessoa);
                }
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
        for (Pessoa pessoa : pessoas) {
            LinearLayout checkboxInnerContainer = new LinearLayout(this);
            checkboxInnerContainer.setOrientation(LinearLayout.HORIZONTAL);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(pessoa.getNome());
            checkBox.setId(pessoa.getId());
            QuantityView quantityView = new QuantityView(this);
            quantityView.setId(pessoa.getId());
            checkboxInnerContainer.addView(checkBox);
            checkboxInnerContainer.addView(quantityView);
            checkboxOuterContainer.addView(checkboxInnerContainer);

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

    void checkFieldsForEmptyValues() {
        String s1 = nomeT.getText().toString();
        String s2 = precoT.getText().toString();

        if (s1.equals("") || s2.equals("")) {
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
    @Override
    public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
        Toast.makeText(this, "Quantity: " + newQuantity, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onLimitReached() {
        Log.d(getClass().getSimpleName(), "Limit reached");
    }
}
