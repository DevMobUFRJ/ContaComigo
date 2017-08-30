package com.devmob.contacomigo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
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
    public static boolean itemAdicionado;
    private String nomeFragmento = "Item";
    private boolean atualizar = true;
    public static Spinner gorjetaSpinner;


    public String getNome(){
        return nomeFragmento;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.items_layout, container, false);

        //Inicialização
        switchGorjeta = (SwitchCompat) view.findViewById(R.id.switchGorjeta);

        itemsExpandableListView = (ExpandableListView) view.findViewById(R.id.produtosExpandableListView);
        ProdutoDAO dao = new ProdutoDAO(getActivity());
        produtos = dao.buscaProdutos();
        listAdapter = new ProdutoExpandableListAdapter(getActivity(), new ArrayList<>(produtos));
        itemsExpandableListView.setAdapter(listAdapter);
        addFAB = (FloatingActionButton) view.findViewById(R.id.addFAB);
        gorjeta = PessoaFragmento.gorjeta;
        gorjetaSpinner = (Spinner) view.findViewById(R.id.gorjetaSpinner);
        SharedPreferences prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        switchGorjeta.setChecked(prefs.getBoolean("switchGorjeta", false));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gorjetas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gorjetaSpinner.setAdapter(adapter);
        gorjetaSpinner.setOnItemSelectedListener(gorjetaSpinnerListener);
        gorjetaSpinner.setSelection(prefs.getInt("gorjetaValor", 9));


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

                if (!buttonView.isChecked()) {
                    Log.d(TAG, "onCheckedChanged: FALSEI ITEM");
                        ((TextView)gorjetaSpinner.getChildAt(0)).setTextColor(Color.BLACK);
                        gorjeta.setAtivo(false);
                        ((TextView)PessoaFragmento.gorjetaSpinner.getChildAt(0)).setTextColor(Color.BLACK);
                        PessoaFragmento.gorjeta.setAtivo(false);
                        PessoaFragmento.switchGorjeta.setChecked(false);
                        listAdapter.notifyDataSetChanged();
                }
                else{
                        Log.d(TAG, "onCheckedChanged: AGORA É TRUE ITEM");
                        ((TextView)gorjetaSpinner.getChildAt(0)).setTextColor(Color.RED);
                        gorjeta.setAtivo(true);
                        ((TextView)PessoaFragmento.gorjetaSpinner.getChildAt(0)).setTextColor(Color.RED);
                        PessoaFragmento.gorjeta.setAtivo(true);
                        PessoaFragmento.switchGorjeta.setChecked(true);
                        listAdapter.notifyDataSetChanged();
                }
            }

        });

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        fragmentBecameVisible();
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor prefEditor = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();
        prefEditor.putInt("gorjetaValor", gorjetaSpinner.getSelectedItemPosition());
        prefEditor.putInt("gorjetaPorcentagem", gorjeta.getPorcentagem());
        prefEditor.putBoolean("gorjetaAtivo", gorjeta.getAtivo());
        prefEditor.commit();
    }


    private void telaAdicionar() {
        Intent intent = new Intent(getActivity(), AddProdutoActivity.class);
        startActivityForResult(intent, 1);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                itemAdicionado = data.getExtras().getBoolean("booleanItem");
                PessoaFragmento.itemAdicionado = data.getExtras().getBoolean("booleanItem");
            }
        }

    }


    @Override
    public void setAtualizar(boolean b) {
        this.atualizar = b;
    }

    public void forceReload(){
        this.atualizar = true;
        fragmentBecameVisible();
    }

    @Override
    public void fragmentBecameVisible() {
        Log.d(TAG, "atualizar is: "+atualizar);
        if(atualizar){
            ProdutoDAO pdao = new ProdutoDAO(getContext());
            produtos = pdao.buscaProdutos();
            listAdapter.resetaLista(produtos);
            listAdapter.notifyDataSetChanged();
            atualizar = false;
            Log.i(TAG, "ItemFrag Atualizada");
        }
    }
    Spinner.OnItemSelectedListener gorjetaSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemSelected: " + parent.getItemAtPosition(position).toString());
            ((TextView) parent.getChildAt(0)).setText(parent.getItemAtPosition(position).toString() + "%");
            ((TextView) parent.getChildAt(0)).setTypeface(null, Typeface.BOLD);
            ((TextView) parent.getChildAt(0)).setTextSize(18);
            gorjeta.setPorcentagem(Integer.parseInt(parent.getItemAtPosition(position).toString()));
            PessoaFragmento.gorjeta.setPorcentagem(Integer.parseInt(parent.getItemAtPosition(position).toString()));
            PessoaFragmento.gorjetaSpinner.setSelection(position);
            listAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}


