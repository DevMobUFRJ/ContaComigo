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
import android.widget.TextView;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.dao.PessoaProdutoDAO;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.MascaraDinheiro;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;
import com.devmob.contacomigo.model.ProdutoConsumido;

import java.text.NumberFormat;
import java.util.ArrayList;
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

    public void iniciaValoresParaEdit(EditText nomeProduto, EditText precoProduto, ViewGroup checkboxOuterContainer, CheckBox quantidadeDiferenteCheck, QuantityView quantityViewTotal){
        PessoaProdutoDAO ppd = new PessoaProdutoDAO(AddProdutoActivity.this);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        ProdutoDAO dao = new ProdutoDAO(AddProdutoActivity.this);
        int id = getIntent().getIntExtra("produtoId", -1);
        Produto produto = dao.getProdutoById(id);
        quantityViewTotal.setQuantity(produto.getQuantidade());
        nomeProduto.setText(produto.getNome());
        precoProduto.setText(new Double(produto.getPreco()).toString());
        precoProduto.setText(nf.format(Float.parseFloat(new Float(produto.getPreco()).toString())));
        PessoaDAO pedao = new PessoaDAO(AddProdutoActivity.this);


        List<Integer> qntds = new ArrayList<>();
        for(int i=0; i < checkboxOuterContainer.getChildCount(); i++){
            View checkbox = ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);

            Pessoa pessoa = pedao.getPessoaById(checkbox.getId());
            Log.d(TAG, "nome pessoa: "+pessoa.getNome());
            ProdutoConsumido pc = ppd.buscaProdutoDeUmaPessoa(pessoa, produto);

            if(pc != null) {
                if (checkbox instanceof CheckBox) {
                    ((CheckBox) checkbox).setChecked(true);
                }
                qntds.add(pc.getQuantidade());
            }
        }
        boolean qntdDiferente = false;
        for(Integer i : qntds){
            if(i!=1) qntdDiferente = true;
        }
        Log.d(TAG, "qntdDiferente: "+qntdDiferente);
        if(qntdDiferente) {
            for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                quantidadeDiferenteCheck.setChecked(true);
                View quantityviewIndividual = ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                quantityviewIndividual.setVisibility(View.VISIBLE);
                Pessoa pessoa = pedao.getPessoaById(quantityviewIndividual.getId());
                Log.d(TAG, "nome pessoa: "+pessoa.getNome());
                ProdutoConsumido pc = ppd.buscaProdutoDeUmaPessoa(pessoa, produto);
                if(pc!=null)
                ((QuantityView)quantityviewIndividual).setQuantity(pc.getQuantidade());
                Log.d(TAG, "iniciaValoresParaEdit: ");
            }
        }
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
        precoProduto.addTextChangedListener(new MascaraDinheiro(precoProduto));

        checkFieldsValues();
        quantityViewTotal.setOnQuantityChangeListener(onQuantityViewTotalChangedListener);
        botaoSalvar.setOnClickListener(botaoSalvarListener);

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("booleanItem", false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        quantityViewTotal.setOnQuantityChangeListener(onQuantityViewTotalChangedListener);
        botaoSalvar.setOnClickListener(botaoSalvarListener);

        PessoaDAO pessoaDAO = new PessoaDAO(AddProdutoActivity.this);

        final List<Pessoa> pessoas = new ArrayList<>(pessoaDAO.buscaPessoas());
        for(Pessoa pessoa : pessoas) {
            LinearLayout checkboxInnerContainer = new LinearLayout(this);
            checkboxInnerContainer.setOrientation(LinearLayout.HORIZONTAL);
            final CheckBox checkBox = new CheckBox(this);
            checkBox.setText(pessoa.getNome());
            checkBox.setId(pessoa.getId());
            checkBox.setOnCheckedChangeListener(checkboxPessoaChangedListener);
            final QuantityView quantityViewIndividual = new QuantityView(this);
            quantityViewIndividual.setId(pessoa.getId());
            quantityViewIndividual.setVisibility(View.INVISIBLE);
            quantityViewIndividual.setQuantity(0);
            quantityViewIndividual.setMaxQuantity(0);
            quantityViewIndividual.setMinQuantity(0);
            quantityViewIndividual.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
                //Não criei listener separado pois precisava do ID do quantitiViewIndividual que chamou a função e não enconteri como contornar.
                @Override
                public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                    onQuantityViewIndividualQuantityChanged(oldQuantity, newQuantity, programmatically, quantityViewIndividual.getId());
                }

                @Override
                public void onLimitReached() {

                }
            });
            final TextView warning = new TextView(this);
            warning.setText("Erro!");
            warning.setVisibility(View.INVISIBLE);
            checkboxInnerContainer.addView(checkBox);
            checkboxInnerContainer.addView(quantityViewIndividual);
            checkboxInnerContainer.addView(warning);
            checkboxOuterContainer.addView(checkboxInnerContainer);
        }
        quantidadeDiferenteCheck.setOnCheckedChangeListener(quantidadeDiferenteListener);

        boolean hasEdit =  getIntent().hasExtra("isEdit");
        if(hasEdit){
            boolean isEdit = (boolean) intent.getExtras().get("isEdit");
            if(isEdit) {
                this.isEdit = isEdit;
                iniciaValoresParaEdit(nomeProduto, precoProduto, checkboxOuterContainer, quantidadeDiferenteCheck, quantityViewTotal);
            }
        }


    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------


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


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    View.OnClickListener botaoSalvarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PessoaProdutoDAO ppd = new PessoaProdutoDAO(AddProdutoActivity.this);
            if(isEdit){
                ProdutoDAO dao = new ProdutoDAO(AddProdutoActivity.this);
                int id = getIntent().getIntExtra("produtoId", -1);
                Produto produto = dao.getProdutoById(id);

                String nome = nomeProduto.getText().toString();
                float preco = stringMonetarioToDouble(precoProduto.getText().toString());
                int quantidadeProdutos = quantityViewTotal.getQuantity();
                produto.setQuantidade(quantidadeProdutos);
                Log.d(TAG, "qntd produtos"+quantidadeProdutos);
                produto.setNome(nome);
                produto.setPreco(preco);

                dao.editaProduto(produto);
                ppd.deletaRelacoesDoProduto(produto);
                salvaConsumidoresDoProduto(ppd, produto);
            }
            else {
                ProdutoDAO produtodao = new ProdutoDAO(AddProdutoActivity.this);
                String nome = nomeProduto.getText().toString();
                float preco = stringMonetarioToDouble(precoProduto.getText().toString());
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
    };

    CompoundButton.OnCheckedChangeListener checkboxPessoaChangedListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //Pega o quantity view relativo ao checkbox
            QuantityView qvDaPessoa = new QuantityView(AddProdutoActivity.this);
            TextView avisoDaPessoa = new TextView(AddProdutoActivity.this);
            for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                qvDaPessoa = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                avisoDaPessoa = (TextView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(2);
                if (qvDaPessoa.getId() == buttonView.getId()){
                    break;
                }
            }
            if (isChecked && qvDaPessoa.getQuantity() == 0 && quantidadeDiferenteCheck.isChecked()){
                avisoDaPessoa.setVisibility(View.VISIBLE);
            }
            else{
                avisoDaPessoa.setVisibility(View.INVISIBLE);
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
                    if (qv.getId() != buttonView.getId()) {
                        if (!outroCheckBox.isChecked() && (outroCheckBox.getId() != buttonView.getId())){
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
            checkFieldsValues();
            if (!isChecked)
                qvDaPessoa.setQuantity(0);
        }
    };



    public void onQuantityViewIndividualQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically, int id) {
        //pega o Checkbox relativo ao quantityView
        CheckBox checkBoxPessoa = new CheckBox(AddProdutoActivity.this);
        TextView avisoDaPessoa = new TextView(AddProdutoActivity.this);
        for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
            checkBoxPessoa = (CheckBox) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
            avisoDaPessoa = (TextView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(2);
            if (checkBoxPessoa.getId() == id){
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
        if (checkBoxPessoa.isChecked() && newQuantity == 0 && quantidadeDiferenteCheck.isChecked()){
            avisoDaPessoa.setVisibility(View.VISIBLE);
        }
        else{
            avisoDaPessoa.setVisibility(View.INVISIBLE);
        }
        //seta máximos
        for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
            QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
            CheckBox outroCheckBox = (CheckBox) ((LinearLayout) checkboxOuterContainer.getChildAt(i)).getChildAt(0);
            if (checkBoxPessoa.isChecked()){
                if (qv.getId() != id) {
                    if (!outroCheckBox.isChecked())
                        qv.setMaxQuantity(quantityViewTotal.getQuantity() - quantidadeContabilizada);
                    else
                        qv.setMaxQuantity(qv.getQuantity() + (quantityViewTotal.getQuantity() - quantidadeContabilizada));
                }
            }
            if (qv.getId() != checkBoxPessoa.getId()) {
                if (!outroCheckBox.isChecked() && (outroCheckBox.getId() != checkBoxPessoa.getId())){
                    if (qv.getQuantity() > qv.getMaxQuantity()) {
                        qv.setQuantity(0);
                    }
                }
            }
            qv.setMinQuantity(0);
        }

        checkFieldsValues();
    }

    CompoundButton.OnCheckedChangeListener quantidadeDiferenteListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked){
                for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                    QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                    TextView avisoDaPessoa = (TextView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(2);
                    avisoDaPessoa.setVisibility(View.INVISIBLE);
                    qv.setVisibility(View.INVISIBLE);
                    qv.setMaxQuantity(1);
                    qv.setMinQuantity(0);
                    qv.setQuantity(0);
                }
            }
            else{
                TextView avisoDaPessoa = new TextView(AddProdutoActivity.this);
                int quantidadeContabilizada = 0;
                for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
                    CheckBox checkBoxPessoa = (CheckBox) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
                    QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
                    avisoDaPessoa = (TextView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(2);
                    qv.setVisibility(View.VISIBLE);
                    if (checkBoxPessoa.isChecked()){
                        quantidadeContabilizada+= qv.getQuantity();
                        if (qv.getQuantity() == 0 && isChecked){
                            avisoDaPessoa.setVisibility(View.VISIBLE);
                        }
                        else{
                            avisoDaPessoa.setVisibility(View.INVISIBLE);
                        }
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
            checkFieldsValues();
        }
    };

    QuantityView.OnQuantityChangeListener onQuantityViewTotalChangedListener = new QuantityView.OnQuantityChangeListener() {
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
            checkFieldsValues();
        }

        @Override
        public void onLimitReached() {
            Log.d(getClass().getSimpleName(), "Limit reached");
        }
    };

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
            checkFieldsValues();
        }
    };

    void checkFieldsValues() {
        String s1 = nomeProduto.getText().toString();
        String s2 = precoProduto.getText().toString();
        boolean temZero = false;

        int quantidadeContabilizada = 0, quantidadeDePessoas = 0;
        for (int i = 0; i < checkboxOuterContainer.getChildCount(); i++) {
            QuantityView qv = (QuantityView) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(1);
            CheckBox checkBoxPessoa = (CheckBox) ((LinearLayout)checkboxOuterContainer.getChildAt(i)).getChildAt(0);
            if (checkBoxPessoa.isChecked()){
                quantidadeContabilizada+= qv.getQuantity();
                quantidadeDePessoas++;
                if (qv.getQuantity() == 0 && quantidadeDiferenteCheck.isChecked()) {
                    temZero = true;
                    break;
                }
            }
        }

        if (s1.equals("") || s2.equals("") || s2.equals("R$")  || s2.equals("US$") || (quantidadeDiferenteCheck.isChecked()
                && quantidadeContabilizada != quantityViewTotal.getQuantity()) || temZero || quantidadeDePessoas == 0) {
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

    public static float stringMonetarioToDouble(String str) {
        float retorno = 0;
        try {
            boolean hasMask = ((str.indexOf("US$") > -1 ||str.indexOf("R$") > -1 || str.indexOf("$") > -1) && (str
                    .indexOf(".") > -1 || str.indexOf(",") > -1));
            // Verificamos se existe máscara
            if (hasMask) {
                Log.d(TAG, "stringMonetarioToDouble: " + str);
                // Retiramos a máscara.
                //str = str.replaceAll("[R$]", "").replaceAll("\\,\\w+", "").replaceAll("\\.\\w+", "");
                str = str.replaceAll("[R$]", "").replaceAll("[US$]", "").replaceAll("[,]", "").replaceAll("[.]", "");
            }

            // Transformamos o número que está escrito no EditText em
            // double.
            Log.d(TAG, "stringMonetarioToDouble: " + str);
            retorno = Float.parseFloat(str);
            retorno/=100;
        } catch (NumberFormatException e) {
            //TRATAR EXCEÇÃO
        }
        return retorno;
    }

}