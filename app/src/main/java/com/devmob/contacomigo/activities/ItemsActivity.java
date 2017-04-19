package com.devmob.contacomigo.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.devmob.contacomigo.ExpandableList.ExpandableListAdapter;
import com.devmob.contacomigo.ExpandableList.PessoaInfo;
import com.devmob.contacomigo.ExpandableList.ProdutoInfo;
import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.Gorjeta;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ItemsActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

    private LinkedHashMap<String, ProdutoInfo> hashProduto = new LinkedHashMap<String, ProdutoInfo>();
    private LinkedHashMap<ProdutoInfo, String> hashNomeProduto = new LinkedHashMap<ProdutoInfo, String>();
    private Map<ProdutoInfo, Integer> hashPessoaProduto = new HashMap<ProdutoInfo, Integer>();
    private Map<ProdutoInfo, Double> hashPrecoProduto = new HashMap<ProdutoInfo, Double>();
    private ArrayList<ProdutoInfo> listProduto = new ArrayList<ProdutoInfo>();

    private ExpandableListAdapter listAdapter;
    //private ExpandableListView itemsExpandableListView;
    public SwitchCompat switchGorjeta;
    private ExpandableListView itemsExpandableListView;
    public FloatingActionButton addFAB; //On Click não funciona com butterknife
    public static Gorjeta gorjeta;
    private TextView gorjetaValor;
    private int qntdDeProdutos = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gorjetaValor = (TextView) findViewById(R.id.gorjetaValor);
        //Inicialização
        carregamentoDeDados();
        switchGorjeta = (SwitchCompat) findViewById(R.id.switchGorjeta);
        gorjetaValor = (TextView) findViewById(R.id.gorjetaValor);
        gorjeta = new Gorjeta();
        itemsExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        listAdapter = new ExpandableListAdapter(ItemsActivity.this, listProduto);
        itemsExpandableListView.setAdapter(listAdapter);
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        addFAB = (FloatingActionButton) findViewById(R.id.addFAB);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.moneyIcon:
                                //textFavorites.setVisibility(View.VISIBLE);
                                //textSchedules.setVisibility(View.GONE);
                                //item.setIcon(R.drawable.ic_people_black_48dp);
                                bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                                item.setChecked(true);
                                Toast.makeText(ItemsActivity.this, "Money", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.personIcon:
                                bottomNavigationView.setItemBackgroundResource(R.color.red);
                                item.setChecked(true);
                                Toast.makeText(ItemsActivity.this, "Person", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tempIcon:
                                bottomNavigationView.setItemBackgroundResource(R.color.black);
                                item.setChecked(true);
                                Toast.makeText(ItemsActivity.this, "tempIcon", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });


        //LONG CLICK EM CADA CHILD (PESSOA E PREÇO)
        itemsExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int indiceProduto = ExpandableListView.getPackedPositionGroup(id);
                    int indicePessoa = ExpandableListView.getPackedPositionChild(id);
                    //get the group header
                    ProdutoInfo produtoInfo = listProduto.get(indiceProduto);
                    //get the child info
                    PessoaInfo detailInfo = produtoInfo.getListProduto().get(indicePessoa);
                    Toast.makeText(ItemsActivity.this, detailInfo.getNomePessoa() + "/" + indicePessoa + " deve " + detailInfo.getPreco(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });
        // CLICK EM CADA CHILD (PESSOA E PREÇO)
        itemsExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int indiceProduto, int indicePessoa, long id) {
                ProdutoInfo produtoInfo = listProduto.get(indiceProduto);
                PessoaInfo pessoaInfo = produtoInfo.getListProduto().get(indicePessoa);
                Toast.makeText(getBaseContext(), " Clicked on :: " + pessoaInfo.getNomePessoa() + "/" + indiceProduto + "/" + pessoaInfo.getPreco(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // CLICK EM CADA HEADER (PRODUTO E PREÇO)
        itemsExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int indiceProduto, long id) {
                ProdutoInfo produtoInfo = listProduto.get(indiceProduto);
                Toast.makeText(getBaseContext(), " Header is :: " + produtoInfo.getNomeProduto(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ItemsActivity.this, "Add", Toast.LENGTH_SHORT).show();
                telaAdicionar();
                listAdapter.notifyDataSetChanged();
            }
        });


        switchGorjeta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switchGorjeta:
                        if (!isChecked) {
                            gorjetaValor.setTextColor(Color.BLACK);
                            gorjeta.setAtivo(false);
                            listAdapter.notifyDataSetChanged();
                            //Toast.makeText(ItemsActivity.this, String.valueOf(gorjeta.getValor()), Toast.LENGTH_SHORT).show();
                        } else {
                            gorjetaValor.setTextColor(Color.RED);
                            gorjeta.setAtivo(true);
                            listAdapter.notifyDataSetChanged();
                            //Toast.makeText(ItemsActivity.this, String.valueOf(gorjeta.getValor()), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        gorjetaValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGorjetaValor();

            }
        });

    }

    private void showGorjetaValor() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialogo, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final NumberPicker np = (NumberPicker) promptsView.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setValue(gorjeta.getPorcentagem());
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        alertDialogBuilder
                .setTitle(R.string.text_tip)
                .setCancelable(false)
                .setPositiveButton(R.string.text_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                gorjetaValor.setText(String.valueOf(np.getValue()) + "%");
                                gorjeta.setPorcentagem(np.getValue());
                                listAdapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton(R.string.text_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }

                );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }


    //DATA SETADA POR HARDCODING TEMPORARIO
    private void carregamentoDeDados() {

        System.out.println("Entrei");

        Pessoa william = new Pessoa("William", 1);
        Pessoa silvio = new Pessoa("Silvio", 2);
        Pessoa daniel = new Pessoa("Daniel", 3);

        ProdutoDAO dao = new ProdutoDAO(this);
        List<Produto> produtos = dao.buscaProdutos();
        dao.close();

        ProdutoInfo teste = new ProdutoInfo();
        for (Produto produto : produtos) {
            System.out.println(produto.getNome());
            teste = adicionaProduto(produto);
            adicionaPessoa(william, teste);
            adicionaPessoa(daniel, teste);
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        ProdutoDAO dao = new ProdutoDAO(this);
        List<Produto> produtos = dao.buscaProdutos();
        if (produtos.size() > qntdDeProdutos) {
            adicionaProduto(produtos.get(produtos.size() - 1));
            listAdapter.notifyDataSetChanged();
        }
    }


    private ProdutoInfo adicionaProduto(Produto produto) {
        String product = produto.getNome();
        double price = produto.getPreco();

        ProdutoInfo produtoInfo;
        produtoInfo = new ProdutoInfo();
        produtoInfo.setProduto(produto);

        produtoInfo.setNomeProduto(product);
        hashNomeProduto.put(produtoInfo, product);
        listProduto.add(produtoInfo);
        hashPessoaProduto.put(produtoInfo, 0);
        hashPrecoProduto.put(produtoInfo, price);
        return produtoInfo;
    }

    private void adicionaPessoa(Pessoa pessoaO, ProdutoInfo produto) {
        String person = pessoaO.getNome();
        double price = hashPrecoProduto.get(produto);
        ArrayList<PessoaInfo> listPessoa = produto.getListProduto();
        //add to the counter
        hashPessoaProduto.put(produto, hashPessoaProduto.get(produto) + 1);
        //create a new child and add that to the group
        PessoaInfo detailInfo = new PessoaInfo();
        detailInfo.setPessoa(pessoaO);
        detailInfo.setNomePessoa(person);
        detailInfo.setPreco(hashPrecoProduto.get(produto) / hashPessoaProduto.get(produto));
        listPessoa.add(detailInfo);
        for (int i = 0; i < listPessoa.size(); i++) {
            listPessoa.get(i).setPreco(hashPrecoProduto.get(produto) / hashPessoaProduto.get(produto));
        }
        produto.setListPessoa(listPessoa);
    }


    //method to expand all groups
    private void expandirTodos() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            itemsExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void fecharTodos() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            itemsExpandableListView.collapseGroup(i);
        }
    }


    private void telaAdicionar() {
        Intent intent = new Intent(this, AddProdutoActivity.class);
        //intent.putExtra(getString(R.string.key_name), name);
        startActivity(intent);

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
    }
}
