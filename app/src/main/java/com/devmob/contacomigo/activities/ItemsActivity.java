package com.devmob.contacomigo.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
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


public class ItemsActivity extends AppCompatActivity {

    private LinkedHashMap<String, ProdutoInfo> hashProduto = new LinkedHashMap<String, ProdutoInfo>();
    private LinkedHashMap<ProdutoInfo, String> FoohashProduto = new LinkedHashMap<ProdutoInfo, String>();
    private Map<ProdutoInfo, Integer> fillMap = new HashMap<ProdutoInfo, Integer>();
    private Map<ProdutoInfo, Double> priceMap = new HashMap<ProdutoInfo, Double>();
    private ArrayList<ProdutoInfo> listProduto = new ArrayList<ProdutoInfo>();

    private ExpandableListAdapter listAdapter;
    //private ExpandableListView itemsExpandableListView;
    public SwitchCompat switchGorjeta;
    private ExpandableListView itemsExpandableListView;
    public FloatingActionButton addFAB; //On Click não funciona com butterknife
    public static Gorjeta gorjeta;
    private int qntdDeProdutos = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Inicialização
        carregamentoDeDados();
        switchGorjeta = (SwitchCompat) findViewById(R.id.switchGorjeta);
        gorjeta = new Gorjeta();
        itemsExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        listAdapter = new ExpandableListAdapter(ItemsActivity.this, listProduto);
        itemsExpandableListView.setAdapter(listAdapter);
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        addFAB = (FloatingActionButton) findViewById(R.id.addFAB);
        //TODO
        //Quando selecionar o ícone, chamar outra janela. Trocar ícone ativo em cada tela. (icone diferente ou mudar bg?)

        //TODO
        //Toolbar
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
                            switchGorjeta.setTextColor(Color.BLACK);
                            gorjeta.setAtivo(false);
                            listAdapter.notifyDataSetChanged();
                            Toast.makeText(ItemsActivity.this, "Err Switch is off!!", Toast.LENGTH_SHORT).show();
                        } else {
                            switchGorjeta.setTextColor(Color.RED);
                            gorjeta.setAtivo(true);
                            listAdapter.notifyDataSetChanged();
                            Toast.makeText(ItemsActivity.this, "Yes Switch is on!!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    //DATA SETADA POR HARDCODING TEMPORARIO
    private void carregamentoDeDados() {

        System.out.println("Entrei");

        Pessoa william = new Pessoa("William", 1);
        Pessoa silvio = new Pessoa("Silvio", 2);
        Pessoa daniel = new Pessoa("Daniel", 3);

        ProdutoDAO dao = new ProdutoDAO(this);
        List<Produto> produtos = dao.buscaProdutos();
        qntdDeProdutos = produtos.size();
        dao.close();

        ProdutoInfo teste = new ProdutoInfo();
        for (Produto produto : produtos) {
            System.out.println(produto.getNome());
            teste = fooAdicionaProduto(produto);
            fooAdicionaPessoa(william, teste);
            fooAdicionaPessoa(daniel, teste);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ProdutoDAO dao = new ProdutoDAO(this);
        List<Produto> produtos = dao.buscaProdutos();
        if (produtos.size() > qntdDeProdutos){
            fooAdicionaProduto(produtos.get(produtos.size() - 1));
            listAdapter.notifyDataSetChanged();
        }
    }
    


    private ProdutoInfo fooAdicionaProduto(Produto produto) {
        String product = produto.getNome();
        double price = produto.getPreco();

        ProdutoInfo produtoInfo;
        produtoInfo = new ProdutoInfo();
        produtoInfo.setProduto(produto);
        FoohashProduto.put(produtoInfo, product);
        listProduto.add(produtoInfo);
        fillMap.put(produtoInfo, 0);
        priceMap.put(produtoInfo, price);
        return produtoInfo;
    }

    private void fooAdicionaPessoa(Pessoa pessoaO, ProdutoInfo produto) {
        String person = pessoaO.getNome();
        double price = priceMap.get(produto);
        ArrayList<PessoaInfo> listPessoa = produto.getListProduto();
        //add to the counter
        fillMap.put(produto, fillMap.get(produto) + 1);
        //create a new child and add that to the group
        PessoaInfo detailInfo = new PessoaInfo();
        detailInfo.setPessoa(pessoaO);
        detailInfo.setNomePessoa(person);
        detailInfo.setPreco(priceMap.get(produto) / fillMap.get(produto));
        listPessoa.add(detailInfo);
        for (int i = 0; i < listPessoa.size(); i++) {
            listPessoa.get(i).setPreco(priceMap.get(produto) / fillMap.get(produto));
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
}