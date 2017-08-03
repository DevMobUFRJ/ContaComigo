package com.devmob.contacomigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.dao.PessoaProdutoDAO;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;
import com.devmob.contacomigo.model.ProdutoConsumido;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.himanshusoni.quantityview.QuantityView;

public class AddProdutoActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener{

    private static final String TAG = "AddProdutoActivity";

    public EditText nomeT;
    public EditText precoT;
    public Button botaoSalvar;
    public Button botaoCancelar;
    ViewGroup checkboxOuterContainer;
    CheckBox quantidadeDiferenteCheck;
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
        quantidadeDiferenteCheck = (CheckBox) findViewById(R.id.quantidadeDiferenteCheckbox);
        checkboxOuterContainer = (ViewGroup) findViewById(R.id.checkbox_outer_container);
        nomeT.addTextChangedListener(mTextWatcher);
        precoT.addTextChangedListener(mTextWatcher);
        checkFieldsForEmptyValues();
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
                Map<Integer, Integer> idsConsumidores = new ArrayMap<Integer, Integer>();
                //TODO getQuantityPerPerson
                //QuantitiView está em View v = ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                //O que muda: precoPorPessoa tera calculo mais complexo
                //"quantidadeConsumida" vai passar o que pega acima
                
                for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                    View v = ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                    View v2 = ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);

                    if (v instanceof CheckBox) {
                        if (((CheckBox) v).isChecked()) {
                            idsConsumidores.put(v.getId(), ((QuantityView) v2).getQuantity());
                        }
                    }
                }

                if (!quantidadeDiferenteCheck.isChecked()){
                    float precoPorPessoa = (float)(produto.getPreco()*produto.getQuantidade()/idsConsumidores.size());

                    for (Integer id : idsConsumidores.keySet()) {
                        int quantidadeConsumida = 1;
                        Log.d(TAG, "onClick : " + id + " " + quantidadeConsumida);
                        ppd.insere(id, produto.getId(), quantidadeConsumida, precoPorPessoa);
                    }
                }
                else{
                    for (Integer id : idsConsumidores.keySet()) {
                        int quantidadeConsumidaPorPessoa = idsConsumidores.get(id);
                        Log.d(TAG, "Pessoa: " + id + " Consumiu: " + quantidadeConsumidaPorPessoa);
                        ppd.insere(id, produto.getId(), quantidadeConsumidaPorPessoa, (float) produto.getPreco() * quantidadeConsumidaPorPessoa);
                        Log.d(TAG, "Preço: "+ (float) produto.getPreco() * quantidadeConsumidaPorPessoa);
                    }
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

        final List<Pessoa> pessoas = pessoaDAO.buscaPessoas();
        for (Pessoa pessoa : pessoas) {
            LinearLayout checkboxInnerContainer = new LinearLayout(this);
            checkboxInnerContainer.setOrientation(LinearLayout.HORIZONTAL);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(pessoa.getNome());
            checkBox.setId(pessoa.getId());
            QuantityView quantityView = new QuantityView(this);
            quantityView.setId(pessoa.getId());
            quantityView.setQuantity(1);
            quantityView.setMaxQuantity(100);
            quantityView.setMinQuantity(1);
            checkboxInnerContainer.addView(checkBox);
            checkboxInnerContainer.addView(quantityView);
            checkboxOuterContainer.addView(checkboxInnerContainer);

        }
        quantidadeDiferenteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        qv.setQuantity(1);
                        qv.setMaxQuantity(1);
                        qv.setMinQuantity(1);
                    }
                }
                else{
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        qv.setQuantity(1);
                        qv.setMaxQuantity(100);
                        qv.setMinQuantity(1);
                    }
                }
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
