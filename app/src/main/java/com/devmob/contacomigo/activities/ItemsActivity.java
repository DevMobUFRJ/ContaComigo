package com.devmob.contacomigo.activities;


import android.content.ClipData;
import android.content.Context;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.devmob.contacomigo.ExpandableList.ExpandableListAdapter;
import com.devmob.contacomigo.ExpandableList.ProdutoInfo;
import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.PessoaDAO;
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

    private LinkedHashMap<Integer, Produto> produtos = new LinkedHashMap<>();

    private ExpandableListAdapter listAdapter;
    //private ExpandableListView itemsExpandableListView;
    public SwitchCompat switchGorjeta;
    private ExpandableListView itemsExpandableListView;
    public FloatingActionButton addFAB; //On Click não funciona com butterknife
    public Button apagaTudo;

    public static Gorjeta gorjeta;
    private TextView gorjetaValor;
    private int qntdDeProdutos = 0;
    boolean itemAdicionado;
    Intent intent;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gorjetaValor = (TextView) findViewById(R.id.gorjetaValor);
        //Inicialização
        apagaTudo = (Button) findViewById(R.id.deletaTudo);
        carregamentoDeDados();
        switchGorjeta = (SwitchCompat) findViewById(R.id.switchGorjeta);
        gorjetaValor = (TextView) findViewById(R.id.gorjetaValor);
        gorjeta = new Gorjeta();
        itemsExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        listAdapter = new ExpandableListAdapter(ItemsActivity.this, new ArrayList<>(produtos.values()));
        itemsExpandableListView.setAdapter(listAdapter);
        addFAB = (FloatingActionButton) findViewById(R.id.addFAB);
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
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
                                intent = new Intent(ItemsActivity.this, PessoasActivity.class);
                                startActivityForResult(intent, 2);
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
                    List<Produto> listProdutos = new ArrayList<Produto>(produtos.values());
                    Produto produto = listProdutos.get(indiceProduto);
                    //get the child info
                    Pessoa pessoa = produto.getConsumidores().get(indicePessoa);
                    Toast.makeText(ItemsActivity.this, pessoa.getNome() + "/" + indicePessoa + " deve " + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });


        // CLICK EM CADA CHILD (PESSOA E PREÇO)
        itemsExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int indiceProduto, int indicePessoa, long id) {
                List<Produto> listProdutos = new ArrayList<Produto>(produtos.values());
                Produto produto = listProdutos.get(indiceProduto);
                Pessoa pessoa = produto.getConsumidores().get(indicePessoa);
                Toast.makeText(getBaseContext(), " Clicked on :: " + pessoa.getNome() + "/" + indiceProduto + "/" + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // CLICK EM CADA HEADER (PRODUTO E PREÇO)
        itemsExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int indiceProduto, long id) {
                List<Produto> listProdutos = new ArrayList<Produto>(produtos.values());
                Produto produto = listProdutos.get(indiceProduto);
                Toast.makeText(getBaseContext(), " Header is :: " + produto.getNome(), Toast.LENGTH_SHORT).show();
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


        final Context aux = this;
        apagaTudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdutoDAO dao = new ProdutoDAO(aux);
                dao.deletaTudo();
                dao.close();
                listAdapter.notifyDataSetChanged();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
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
        np.setWrapSelectorWheel(true);
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

        for (Produto produto : produtos) {
            System.out.println(produto.getNome());
            adicionaProduto(produto);
            adicionaPessoa(william, produto);
            adicionaPessoa(daniel, produto);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ProdutoDAO dao = new ProdutoDAO(this);
        List<Produto> produtos = dao.buscaProdutos();
        if (itemAdicionado==true) {
            System.out.println("AEEEEE");
            System.out.println(produtos.get(produtos.size() - 1).getNome());

            adicionaProduto(produtos.get(produtos.size() - 1));
            listAdapter.notifyDataSetChanged();
        }
    }


    private void adicionaProduto(Produto produto) {
        produtos.put(produto.getId(), produto);
    }

    private void adicionaPessoa(Pessoa pessoa, Produto produto) {
        double price = produtos.get(produto.getId()).getPreco();
        List<Pessoa> consumidores = produtos.get(produto.getId()).getConsumidores();
        consumidores.add(pessoa);
        for (Pessoa person : consumidores) {
            person.setPrecoTotal(price/consumidores.size());
        }
    }





    private void telaAdicionar() {
        Intent intent = new Intent(this, AddProdutoActivity.class);
        //intent.putExtra(getString(R.string.key_name), name);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                itemAdicionado = data.getExtras().getBoolean("booleanItem");
                Toast.makeText(this, String.valueOf(itemAdicionado), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
    }
}
