package com.devmob.contacomigo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.dao.PessoaProdutoDAO;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.Gorjeta;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

import me.himanshusoni.quantityview.QuantityView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by devmob on 03/05/17.
 */

public class ItemFragmento extends Fragment implements FragmentInterface{
    private static final String TAG = "ItemFragmento";



    private List<Produto> produtos = new ArrayList<>();
    public static ProdutoExpandableListAdapter listAdapter;
    public static SwitchCompat switchGorjeta;
    private ExpandableListView itemsExpandableListView;
    public FloatingActionButton addFAB;
    public static Gorjeta gorjeta;
    public static TextView gorjetaValor;
    public static boolean itemAdicionado;
    private String nomeFragmento = "Item";
    private boolean atualizar = true;

    public String getNome(){
        return nomeFragmento;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.items_layout, container, false);

        //Inicialização
        switchGorjeta = (SwitchCompat) view.findViewById(R.id.switchGorjeta);
        gorjetaValor = (TextView) view.findViewById(R.id.gorjetaValor);

        itemsExpandableListView = (ExpandableListView) view.findViewById(R.id.produtosExpandableListView);
        ProdutoDAO dao = new ProdutoDAO(getActivity());
        produtos = dao.buscaProdutos();
        listAdapter = new ProdutoExpandableListAdapter(getActivity(), new ArrayList<>(produtos));
        itemsExpandableListView.setAdapter(listAdapter);
        addFAB = (FloatingActionButton) view.findViewById(R.id.addFAB);

        SharedPreferences prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

            gorjetaValor.setText(prefs.getString("gorjetaValor", "10%"));
            gorjeta = new Gorjeta(prefs.getInt("gorjetaPorcentagem", 10), prefs.getBoolean("gorjetaAtivo", false));
            switchGorjeta.setChecked(prefs.getBoolean("switchGorjeta", false));



        //LONG CLICK EM CADA CHILD (PESSOA E PREÇO)
        itemsExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int indiceProduto = ExpandableListView.getPackedPositionGroup(id);
                int indicePessoa = ExpandableListView.getPackedPositionChild(id);
                //get the group header
                Produto produto = ItemFragmento.this.produtos.get(indiceProduto);
                //LONG CLICK NA PESSOA
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Pessoa pessoa = produto.getConsumidores().get(indicePessoa);
                    //Toast.makeText(getActivity(), pessoa.getNome() + "/" + indicePessoa + " deve " + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                //LONG CLICK NO PRODUTO
                else if(ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
                    //Toast.makeText(getActivity(), produto.getNome() + " " + produto.getPreco(), Toast.LENGTH_SHORT).show();
                    BottomSheetMenuProduto bottomSheetDialogFragment = new BottomSheetMenuProduto();
                    bottomSheetDialogFragment.setItemFragmento(ItemFragmento.this);
                    bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                    Bundle b = new Bundle();
                    b.putInt("idProd", produto.getId());
                    bottomSheetDialogFragment.setArguments(b);
                    return true;
                }

                return false;
            }
        });
        // CLICK EM CADA CHILD (PESSOA E PREÇO)
        itemsExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int indiceProduto, int indicePessoa, long id) {
                ProdutoDAO dao = new ProdutoDAO(getActivity());
                List<Produto> produtos = dao.buscaProdutos();
                dao.close();
                Produto produto = produtos.get(indiceProduto);
                Pessoa pessoa = produto.getConsumidores().get(indicePessoa);
                //Toast.makeText(getActivity(), " Clicked on :: " + indiceProduto + "/" + indiceProduto + "/" + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // CLICK EM CADA HEADER (PRODUTO E PREÇO)
        itemsExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int indiceProduto, long id) {
                ProdutoDAO dao = new ProdutoDAO(getActivity());
                List<Produto> produtos = dao.buscaProdutos();
                dao.close();
                if(produtos.get(indiceProduto) != null){
                    //Toast.makeText(getActivity(), "GROUP ID " + indiceProduto + "  size->"+produtos.get(indiceProduto).getConsumidores().size(), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getActivity(), "GROUP ID" + indiceProduto, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //BOTAO ADICIONAR PESSOA
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Add", Toast.LENGTH_SHORT).show();
                telaAdicionar();
                listAdapter.notifyDataSetChanged();
            }
        });



        switchGorjeta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor prefEditor = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();
                prefEditor.putBoolean("switchGorjeta", gorjeta.getAtivo());
                prefEditor.commit();

                if (!buttonView.isChecked()) {
                    Log.d(TAG, "onCheckedChanged: FALSEI ITEM");
                        gorjetaValor.setTextColor(Color.BLACK);
                        gorjeta.setAtivo(false);
                        PessoaFragmento.gorjetaValor.setTextColor(Color.BLACK);
                        PessoaFragmento.gorjeta.setAtivo(false);
                        PessoaFragmento.switchGorjeta.setChecked(false);
                        listAdapter.notifyDataSetChanged();
                }
                else{
                        Log.d(TAG, "onCheckedChanged: AGORA É TRUE ITEM");
                        gorjetaValor.setTextColor(Color.RED);
                        gorjeta.setAtivo(true);
                        PessoaFragmento.gorjetaValor.setTextColor(Color.RED);
                        PessoaFragmento.gorjeta.setAtivo(true);
                        PessoaFragmento.switchGorjeta.setChecked(true);
                        listAdapter.notifyDataSetChanged();
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
                                PessoaFragmento.gorjetaValor.setText(String.valueOf(np.getValue()) + "%");
                                PessoaFragmento.gorjeta.setPorcentagem(np.getValue());
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

    public void adicionaProduto(Produto produto){
        produtos.add(produto);
        listAdapter.insereProdutoNaLista(produto);

    }

    @Override
    public void onResume() {
        super.onResume();
        //busca produtos
        Log.d(TAG, "onResume");
        ProdutoDAO pdao = new ProdutoDAO(getActivity());
        produtos = pdao.buscaProdutos();
        if (itemAdicionado==true) {
            //pega ultimo produto adicionado
            Produto produto = produtos.get(produtos.size() - 1);

            //pega consumidores relacionados a esse produto
            PessoaProdutoDAO ppdao = new PessoaProdutoDAO(getActivity());
            List<Pessoa> consumidores = ppdao.buscaPessoasDeUmProduto(produto);

            produto.setConsumidores(consumidores);

            adicionaProduto(produto);
            itemAdicionado = false;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor prefEditor = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();
        prefEditor.putString("gorjetaValor", gorjetaValor .getText().toString());
        prefEditor.putInt("gorjetaPorcentagem", gorjeta.getPorcentagem());
        prefEditor.putBoolean("gorjetaAtivo", gorjeta.getAtivo());
        prefEditor.commit();
        Toast.makeText(getActivity(), "Item Fragmento Stopped", Toast.LENGTH_SHORT).show();
    }


    private void telaAdicionar() {
        Intent intent = new Intent(getActivity(), AddProdutoActivity.class);
        //intent.putExtra(getString(R.string.key_name), name);
        startActivityForResult(intent, 1);
    }
    
    public void atualizaListas(){
        ProdutoDAO pdao = new ProdutoDAO(getActivity());
        produtos = pdao.buscaProdutos();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                itemAdicionado = data.getExtras().getBoolean("booleanItem");
                PessoaFragmento.itemAdicionado = data.getExtras().getBoolean("booleanItem");

                listAdapter.notifyDataSetChanged();

            }
        }

    }


    @Override
    public void setAtualizar(boolean b) {
        this.atualizar = b;
    }

    @Override
    public void fragmentBecameVisible() {
        if(atualizar){
            ProdutoDAO pdao = new ProdutoDAO(getContext());
            produtos = pdao.buscaProdutos();
            listAdapter.resetaLista(produtos);
            listAdapter.notifyDataSetChanged();
            atualizar = false;
            Log.i(TAG, "ItemFrag Atualizada");
        }
    }

}


