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

import java.util.List;
import java.util.Map;

import me.himanshusoni.quantityview.QuantityView;

public class AddProdutoActivity extends AppCompatActivity implements QuantityView.OnQuantityChangeListener{

    private static final String TAG = "AddProdutoActivity";

    private EditText nomeProduto;
    private EditText precoProduto;
    private Button botaoSalvar;
    private Button botaoCancelar;
    private ViewGroup checkboxOuterContainer;
    private CheckBox quantidadeDiferenteCheck;
    private QuantityView quantityViewTotal;
    private Intent intent;
    private boolean isEdit = false;

    private void atualizaFragmentos() {
        MainActivity.itemFragmento.setAtualizar(true);
        MainActivity.pessoaFragmento.setAtualizar(true);
        intent.putExtra("booleanItem", true);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);
        intent = getIntent();
        nomeProduto = (EditText) findViewById(R.id.nome);
        precoProduto = (EditText) findViewById(R.id.preco);
        botaoSalvar = (Button) findViewById(R.id.salvar);
        botaoCancelar = (Button) findViewById(R.id.cancelar);
        quantityViewTotal = (QuantityView) findViewById(R.id.quantityView);
        quantidadeDiferenteCheck = (CheckBox) findViewById(R.id.quantidadeDiferenteCheckbox);
        checkboxOuterContainer = (ViewGroup) findViewById(R.id.checkbox_outer_container);
        nomeProduto.addTextChangedListener(mTextWatcher);
        precoProduto.addTextChangedListener(mTextWatcher);

        boolean hasEdit =  getIntent().hasExtra("isEdit");
        if(hasEdit){
            boolean isEdit = (boolean) intent.getExtras().get("isEdit");
            if(isEdit) {
                this.isEdit = isEdit;
                iniciaValoresParaEdit(nomeProduto, precoProduto);
            }
        }

        checkFieldsForEmptyValues();
        quantityViewTotal.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                if (quantidadeDiferenteCheck.isChecked()) {


                    int quantidadeContabilizada = 0;
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        CheckBox checkBoxPessoaDiferente = (CheckBox) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                        if (checkBoxPessoaDiferente.isChecked()) {
                            quantidadeContabilizada += qv.getQuantity();
                        }
                    }
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        CheckBox outroCheckBox = (CheckBox) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);

                        if (!outroCheckBox.isChecked())
                            qv.setMaxQuantity(newQuantity - quantidadeContabilizada);
                        else
                            qv.setMaxQuantity(qv.getQuantity() + (newQuantity - quantidadeContabilizada));
                        qv.setMinQuantity(0);
                    }
                }
            }

            @Override
            public void onLimitReached() {
                Log.d(getClass().getSimpleName(), "Limit reached");
            }
        });
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PessoaProdutoDAO ppd = new PessoaProdutoDAO(AddProdutoActivity.this);
                if(isEdit){
                    ProdutoDAO dao = new ProdutoDAO(AddProdutoActivity.this);
                    int id = getIntent().getIntExtra("produtoId", -1);
                    Produto produto = dao.getProdutoById(id);

                    String nome = nomeProduto.getText().toString();
                    float preco = Float.parseFloat(precoProduto.getText().toString());
                    produto.setNome(nome);
                    produto.setPreco(preco);

                    dao.editaProduto(produto);
                    ppd.deletaRelacoesDoProduto(produto);
                    salvaConsumidoresDoProduto(ppd, produto);
                }
                else {
                    ProdutoDAO produtodao = new ProdutoDAO(AddProdutoActivity.this);
                    String nome = nomeProduto.getText().toString();
                    float preco = Float.parseFloat(precoProduto.getText().toString());
                    int quantidadeProdutos = quantityViewTotal.getQuantity();
                    Log.d(TAG, "onClick: aqui" + quantidadeProdutos);
                    Produto produto = new Produto(nome, preco, quantidadeProdutos);
                    produtodao.insere(produto);
                    produtodao.close();
                    Toast.makeText(AddProdutoActivity.this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    salvaConsumidoresDoProduto(ppd, produto);
                }
                atualizaFragmentos();
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
            final CheckBox checkBox = new CheckBox(this);
            checkBox.setText(pessoa.getNome());
            checkBox.setId(pessoa.getId());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Pega o quantity view relativo ao checkbox
                    QuantityView qvDaPessoa = new QuantityView(AddProdutoActivity.this);
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        qvDaPessoa = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        if (qvDaPessoa.getId() == checkBox.getId()){
                            break;
                        }
                    }
                    //pega a quantidade das pessoas com check ja marcado
                    int quantidadeContabilizada = 0;
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        CheckBox checkBoxPessoaDiferente = (CheckBox) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                        if (checkBoxPessoaDiferente.isChecked() ){
                            quantidadeContabilizada+= qv.getQuantity();
                        }
                    }
                    Toast.makeText(AddProdutoActivity.this, "Contabilizada = " + quantidadeContabilizada, Toast.LENGTH_SHORT).show();

                    //seta o maximo de todos
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        //caso o checkbox tenha sido marcado
                        if (isChecked) {
                            QuantityView qv = (QuantityView) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                            CheckBox outroCheckBox = (CheckBox) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                            if (qv.getId() != qvDaPessoa.getId()) {
                                if (!outroCheckBox.isChecked())
                                    qv.setMaxQuantity(quantityViewTotal.getQuantity() - quantidadeContabilizada);
                                else
                                    qv.setMaxQuantity(qv.getQuantity() + (quantityViewTotal.getQuantity() - quantidadeContabilizada));
                            }
                            qv.setMinQuantity(0);
                            //reseta valor dos outros se estiver maior
                            if (qv.getId() != checkBox.getId()) {
                                if (!outroCheckBox.isChecked() && (outroCheckBox.getId() != checkBox.getId())){
                                    if (qv.getQuantity() > qv.getMaxQuantity()) {
                                        qv.setQuantity(0);
                                    }
                                }
                            }
                        }
                        //caso o checkbox seja desmarcado
                        else{
                            QuantityView qv = (QuantityView) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                            CheckBox outroCheckBox = (CheckBox) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                            if (qv.getId() != qvDaPessoa.getId()) {
                                if (!outroCheckBox.isChecked())
                                    qv.setMaxQuantity(quantityViewTotal.getQuantity() - quantidadeContabilizada);
                                else
                                    qv.setMaxQuantity(qv.getQuantity() + (quantityViewTotal.getQuantity() - quantidadeContabilizada));
                            }
                        }
                    }
                    if (!isChecked)
                      qvDaPessoa.setQuantity(0);
                }
            });
            final QuantityView quantityViewIndividual = new QuantityView(this);
            quantityViewIndividual.setId(pessoa.getId());
            quantityViewIndividual.setQuantity(0);
            quantityViewIndividual.setMaxQuantity(0);
            quantityViewIndividual.setMinQuantity(0);
            quantityViewIndividual.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
                @Override
                public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                    //pega o Checkbox relativo ao quantityView
                    CheckBox checkBoxPessoa = new CheckBox(AddProdutoActivity.this);
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        checkBoxPessoa = (CheckBox) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                        if (checkBoxPessoa.getId() == quantityViewIndividual.getId()){
                            break;
                        }
                    }
                    //Pega a quantidade de todos os Checked
                    int quantidadeContabilizada = 0;
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        CheckBox checkBoxPessoaDiferente = (CheckBox) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                        if (checkBoxPessoaDiferente.isChecked()){
                            quantidadeContabilizada+= qv.getQuantity();
                        }
                    }
                    Toast.makeText(AddProdutoActivity.this, "Contabilizada = " + quantidadeContabilizada, Toast.LENGTH_SHORT).show();
                    //seta máximos
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        CheckBox outroCheckBox = (CheckBox) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                        if (checkBoxPessoa.isChecked()){
                            if (qv.getId() != quantityViewIndividual.getId()) {
                                if (!outroCheckBox.isChecked())
                                    qv.setMaxQuantity(quantityViewTotal.getQuantity() - quantidadeContabilizada);
                                else
                                    qv.setMaxQuantity(qv.getQuantity() + (quantityViewTotal.getQuantity() - quantidadeContabilizada));
                            }
                        }
                        if (qv.getId() != checkBox.getId()) {
                            if (!outroCheckBox.isChecked() && (outroCheckBox.getId() != checkBoxPessoa.getId())){
                                if (qv.getQuantity() > qv.getMaxQuantity()) {
                                    qv.setQuantity(0);
                                }
                            }
                        }
                        qv.setMinQuantity(0);
                    }
                }

                @Override
                public void onLimitReached() {

                }
            });
            checkboxInnerContainer.addView(checkBox);
            checkboxInnerContainer.addView(quantityViewIndividual);
            checkboxOuterContainer.addView(checkboxInnerContainer);

        }
        quantidadeDiferenteCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        qv.setMaxQuantity(1);
                        qv.setMinQuantity(0);
                        qv.setQuantity(0);
                    }
                }
                else{
                    int quantidadeContabilizada = 0;
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        CheckBox checkBoxPessoa = (CheckBox) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                        if (checkBoxPessoa.isChecked()){
                            quantidadeContabilizada+= qv.getQuantity();
                        }
                    }
                    for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                        QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                        CheckBox outroCheckBox = (CheckBox) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);

                        if (!outroCheckBox.isChecked())
                            qv.setMaxQuantity(quantityViewTotal.getQuantity() - quantidadeContabilizada);
                        else
                            qv.setMaxQuantity(qv.getQuantity() + (quantityViewTotal.getQuantity() - quantidadeContabilizada));
                        qv.setMinQuantity(0);
                    }
                }
            }
        });

    }

    private void salvaConsumidoresDoProduto(PessoaProdutoDAO ppd, Produto produto) {
        Map<Integer, Integer> idsConsumidores = new ArrayMap<Integer, Integer>();
        //TODO getQuantityPerPerson
        //QuantitiView está em View v = ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
        //O que muda: precoPorPessoa tera calculo mais complexo
        //"quantidadeConsumida" vai passar o que pega acima

        for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
            View v = ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);
            View v2 = ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(1);

            if (v instanceof CheckBox) {
                if (((CheckBox) v).isChecked()) {
                    idsConsumidores.put(v.getId(), ((QuantityView) v2).getQuantity());
                }
            }
        }

        if (!quantidadeDiferenteCheck.isChecked()) {
            float precoPorPessoa = (float) (produto.getPreco() * produto.getQuantidade() / idsConsumidores.size());

            for (Integer id : idsConsumidores.keySet()) {
                int quantidadeConsumida = 1;
                Log.d(TAG, "onClick : " + id + " " + quantidadeConsumida);
                ppd.insere(id, produto.getId(), quantidadeConsumida, precoPorPessoa);
            }
        } else {
            for (Integer id : idsConsumidores.keySet()) {
                int quantidadeConsumidaPorPessoa = idsConsumidores.get(id);
                Log.d(TAG, "Pessoa: " + id + " Consumiu: " + quantidadeConsumidaPorPessoa);
                ppd.insere(id, produto.getId(), quantidadeConsumidaPorPessoa, (float) produto.getPreco() * quantidadeConsumidaPorPessoa);
                Log.d(TAG, "Preço: " + (float) produto.getPreco() * quantidadeConsumidaPorPessoa);
            }
        }
    }


    public void iniciaValoresParaEdit(EditText nomeProduto, EditText precoProduto){
        ProdutoDAO dao = new ProdutoDAO(AddProdutoActivity.this);
        int id = getIntent().getIntExtra("produtoId", -1);
        Produto produto = dao.getProdutoById(id);
        nomeProduto.setText(produto.getNome());
        precoProduto.setText(new Double(produto.getPreco()).toString());
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
        String s1 = nomeProduto.getText().toString();
        String s2 = precoProduto.getText().toString();

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
