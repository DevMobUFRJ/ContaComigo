package com.devmob.contacomigo.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.devmob.contacomigo.ExpandableList.ProdutoExpandableListAdapter;
import com.devmob.contacomigo.R;
import com.devmob.contacomigo.activities.AddProdutoActivity;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.Gorjeta;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by devmob on 03/05/17.
 */

public class ItemFragmento extends Fragment {
    private static final String TAG = "ItemFragmento";


    private LinkedHashMap<Integer, Produto> produtos = new LinkedHashMap<>();
    public static ProdutoExpandableListAdapter listAdapter;
    public SwitchCompat switchGorjeta;
    private ExpandableListView itemsExpandableListView;
    public FloatingActionButton addFAB;
    public Button apagaTudo;
    public static Gorjeta gorjeta;
    private TextView gorjetaValor;
    private int qntdDeProdutos = 0;
    boolean itemAdicionado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.items_layout, container, false);

        gorjetaValor = (TextView) view.findViewById(R.id.gorjetaValor);
        //Inicialização
        apagaTudo = (Button) view.findViewById(R.id.deletaTudo);
        carregamentoDeDados();
        switchGorjeta = (SwitchCompat) view.findViewById(R.id.switchGorjeta);
        gorjetaValor = (TextView) view.findViewById(R.id.gorjetaValor);
        gorjeta = new Gorjeta();
        itemsExpandableListView = (ExpandableListView) view.findViewById(R.id.produtosExpandableListView);
        listAdapter = new ProdutoExpandableListAdapter(getActivity(), new ArrayList<>(produtos.values()));
        itemsExpandableListView.setAdapter(listAdapter);
        addFAB = (FloatingActionButton) view.findViewById(R.id.addFAB);

        //LONG CLICK EM CADA CHILD (PESSOA E PREÇO)
        itemsExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int indiceProduto = ExpandableListView.getPackedPositionGroup(id);
                int indicePessoa = ExpandableListView.getPackedPositionChild(id);
                //get the group header
                List<Produto> listProdutos = new ArrayList<Produto>(produtos.values());
                Produto produto = listProdutos.get(indiceProduto);
                //LONG CLICK NA PESSOA
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Pessoa pessoa = produto.getConsumidores().get(indicePessoa);
                    Toast.makeText(getActivity(), pessoa.getNome() + "/" + indicePessoa + " deve " + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                //LONG CLICK NO PRODUTO
                else if(ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
                    Toast.makeText(getActivity(), produto.getNome() + " " + produto.getPreco(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), " Clicked on :: " + pessoa.getNome() + "/" + indiceProduto + "/" + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // CLICK EM CADA HEADER (PRODUTO E PREÇO)
        itemsExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int indiceProduto, long id) {
                List<Produto> listProdutos = new ArrayList<Produto>(produtos.values());
                Produto produto = listProdutos.get(indiceProduto);
                Toast.makeText(getActivity(), " Header is :: " + produto.getNome(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
                telaAdicionar();
                listAdapter.notifyDataSetChanged();
            }
        });


        apagaTudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO fazer apagaTudo
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

        return view;
    }



    private void showGorjetaValor() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialogo, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final NumberPicker np = (NumberPicker) promptsView.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setValue(gorjeta.getPorcentagem());
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("value is", "" + newVal);
            }
        });
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

        Pessoa william = new Pessoa(1,"William", 23);
        Pessoa silvio = new Pessoa(2,"Silvio",24);
        Pessoa daniel = new Pessoa(3,"Daniel",2);

        ProdutoDAO dao = new ProdutoDAO(getActivity());
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
        ProdutoDAO dao = new ProdutoDAO(getActivity());
        List<Produto> produtos = dao.buscaProdutos();
        if (itemAdicionado==true) {
            System.out.println("AEEEEE");
            System.out.println(produtos.get(produtos.size() - 1).getNome());
            adicionaProduto(produtos.get(produtos.size() - 1));
            Toast.makeText(getActivity(), "Resumido", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getActivity(), AddProdutoActivity.class);
        //intent.putExtra(getString(R.string.key_name), name);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                itemAdicionado = data.getExtras().getBoolean("booleanItem");
                listAdapter.notifyDataSetChanged();

            }
        }
    }


}


