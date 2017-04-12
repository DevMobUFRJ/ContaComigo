package com.devmob.contacomigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.devmob.contacomigo.ExpandableList.ExpandableListAdapter;
import com.devmob.contacomigo.ExpandableList.PessoaInfo;
import com.devmob.contacomigo.ExpandableList.ProdutoInfo;
import com.devmob.contacomigo.R;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ItemsActivity extends AppCompatActivity {

    private LinkedHashMap<String, ProdutoInfo> hashProduto = new LinkedHashMap<String, ProdutoInfo>();
    private ArrayList<ProdutoInfo> listProduto = new ArrayList<ProdutoInfo>();

    private ExpandableListAdapter listAdapter;
    //private ExpandableListView itemsExpandableListView;

    @BindView(R.id.simpleExpandableListView)
    private ExpandableListView itemsExpandableListView;
    public FloatingActionButton addFAB; //On Click não funciona com butterknife

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //Inicialização
        carregamentoDeDados();
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

        final AppCompatActivity aux = this;
        addFAB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(ItemsActivity.this, "Add", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(aux, AddProdutoActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    //DATA SETADA POR HARDCODING TEMPORARIO
    private void carregamentoDeDados() {

        Pessoa william = new Pessoa("William", 1);
        Pessoa silvio = new Pessoa("Silvio", 2);
        Pessoa daniel = new Pessoa("Daniel", 3);


        Produto batata = new Produto(1, "Aussie Cheese Fries", 48.60f);
        Produto cebola = new Produto(2, "Bloomin'Onion", 48.60f);

        addProduto(william, batata);
        addProduto(silvio, batata);
        addProduto(daniel, batata);

        addProduto(william, cebola);
        addProduto(daniel, cebola);


    }

    //PREENCHIMENTO DE CLASSES
    private int addProduto(Pessoa pessoaO, Produto produto) {

        String product = produto.getNome();
        String person = pessoaO.getNome();
        double price = produto.getPreco();

        int posicaoPessoa = 0;

        //CHECA SE PRODUTO JA EXISTE
        ProdutoInfo produtoInfo = hashProduto.get(product);

        //CASO NÃO EXISTA, CRIA
        if (produtoInfo == null) {
            produtoInfo = new ProdutoInfo();
            produtoInfo.setProduto(produto);
            produtoInfo.setNomeProduto(product);
            hashProduto.put(product, produtoInfo);
            listProduto.add(produtoInfo);
        }

        //get the children for the group
        ArrayList<PessoaInfo> listPessoa = produtoInfo.getListProduto();
        //size of the children list
        int listTamanho = listPessoa.size();
        //add to the counter
        listTamanho++;

        //create a new child and add that to the group
        PessoaInfo detailInfo = new PessoaInfo();
        detailInfo.setPessoa(pessoaO);
        detailInfo.setNomePessoa(person);
        detailInfo.setPreco(price);
        listPessoa.add(detailInfo);
        produtoInfo.setListPessoa(listPessoa);

        //find the group position inside the list
        posicaoPessoa = this.listProduto.indexOf(produtoInfo);
        return posicaoPessoa;
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
}
