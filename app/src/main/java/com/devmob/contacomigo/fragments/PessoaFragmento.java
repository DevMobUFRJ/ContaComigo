package com.devmob.contacomigo.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialogFragment;
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

import com.devmob.contacomigo.ExpandableList.PessoaExpandableListAdapter;
import com.devmob.contacomigo.ExpandableList.ProdutoExpandableListAdapter;
import com.devmob.contacomigo.R;
import com.devmob.contacomigo.activities.AddPessoaActivity;
import com.devmob.contacomigo.activities.AddProdutoActivity;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.dao.PessoaProdutoDAO;
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

public class PessoaFragmento extends Fragment {
    private static final String TAG = "ItemFragmento";


    private List<Pessoa> pessoas = new ArrayList<>();
    public static PessoaExpandableListAdapter listAdapter;
    private ExpandableListView pessoasExpandableListView;
    public FloatingActionButton addFAB;
    public Button apagaTudo;
    boolean itemAdicionado;
    public static Gorjeta gorjeta;
    private TextView gorjetaValor;
    public SwitchCompat switchGorjeta;
    private String nomeFragmento = "Pessoa";

    public String getNome(){
        return nomeFragmento;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pessoas_layout, container, false);

        //Inicialização
        apagaTudo = (Button) view.findViewById(R.id.deletaTudo);
        switchGorjeta = (SwitchCompat) view.findViewById(R.id.switchGorjeta);
        gorjetaValor = (TextView) view.findViewById(R.id.gorjetaValor);
        carregamentoDeDados();
        pessoasExpandableListView = (ExpandableListView) view.findViewById(R.id.pessoasExpandableListView);
        PessoaDAO dao = new PessoaDAO(getActivity());
        List<Pessoa> pessoas = dao.buscaPessoas();
        dao.close();

        for (Pessoa p:pessoas){
            Log.d(TAG, p.getNome());
        }

        listAdapter = new PessoaExpandableListAdapter(getActivity(), new ArrayList<>(pessoas));
        pessoasExpandableListView.setAdapter(listAdapter);
        addFAB = (FloatingActionButton) view.findViewById(R.id.addFAB);

        //LONG CLICK EM CADA CHILD (PESSOA E PREÇO)
        pessoasExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int indiceProduto = ExpandableListView.getPackedPositionGroup(id);
                int indicePessoa = ExpandableListView.getPackedPositionChild(id);
                //get the group header
                PessoaDAO dao = new PessoaDAO(getActivity());
                List<Pessoa> pessoas = dao.buscaPessoas();
                dao.close();
                List<Pessoa> listPessoas = new ArrayList<Pessoa>(pessoas);
                Pessoa pessoa = listPessoas.get(indicePessoa);
                //LONG CLICK NA PESSOA
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Produto produto = pessoa.getProdutos().get(indicePessoa);
                    Toast.makeText(getActivity(), pessoa.getNome() + "/" + indicePessoa + " deve " + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                //LONG CLICK NO PRODUTO
                else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    //Toast.makeText(getActivity(), produto.getNome() + " " + produto.getPreco(), Toast.LENGTH_SHORT).show();
                    BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetMenu();
                    bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                    Bundle b = new Bundle();
                    b.putInt("idPessoa", pessoa.getId());
                    bottomSheetDialogFragment.setArguments(b);
                    return true;
                }

                return false;
            }
        });
        // CLICK EM CADA CHILD (PESSOA E PREÇO)
        pessoasExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int indicePessoa, int indiceProduto, long id) {
                PessoaDAO dao = new PessoaDAO(getActivity());
                List<Pessoa> pessoas = dao.buscaPessoas();
                dao.close();
                List<Pessoa> listPessoas = new ArrayList<Pessoa>(pessoas);
                Pessoa pessoa = listPessoas.get(indicePessoa);
                Produto produto = pessoa.getProdutos().get(indiceProduto);
                Toast.makeText(getActivity(), " Clicked on :: " + pessoa.getNome() + "/" + indiceProduto + "/" + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        // CLICK EM CADA HEADER (PRODUTO E PREÇO)
        pessoasExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int indicePessoa, long id) {
                PessoaDAO dao = new PessoaDAO(getActivity());
                List<Pessoa> pessoas = dao.buscaPessoas();
                dao.close();
                List<Pessoa> listPessoas = new ArrayList<Pessoa>(pessoas);
                Pessoa pessoa = listPessoas.get(indicePessoa);
                //Toast.makeText(getActivity(), " Header is :: " + produto.getNome(), Toast.LENGTH_SHORT).show();
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

        //Pessoa william = new Pessoa(1,"William", 23);
        //Pessoa silvio = new Pessoa(2,"Silvio",24);
        //Pessoa daniel = new Pessoa(3,"Daniel",2);

        PessoaDAO dao = new PessoaDAO(getActivity());
        List<Pessoa> pessoas = dao.buscaPessoas();
        dao.close();

        for (Pessoa pessoa : pessoas) {
            System.out.println(pessoa.getNome());
            adicionaPessoa(pessoa);
        }
    }

    public void onResume() {
        super.onResume();
        PessoaDAO dao = new PessoaDAO(getActivity());
        List<Pessoa> pessoas = dao.buscaPessoas();
        if (itemAdicionado == true) {
            System.out.println(pessoas.get(pessoas.size() - 1).getNome());
            adicionaPessoa(pessoas.get(pessoas.size() - 1));
            Toast.makeText(getActivity(), "Resumido", Toast.LENGTH_SHORT).show();
            listAdapter.notifyDataSetChanged();
            itemAdicionado = false;
        }
    }


    private void adicionaPessoa(Pessoa pessoa) {
        pessoas.add(pessoa);
    }

    private void adicionaProduto(Produto produto, Pessoa pessoa) {
        List<Produto> produtos = pessoa.getProdutos();
        produtos.add(produto);

    }


    private void telaAdicionar() {
        Intent intent = new Intent(getActivity(), AddPessoaActivity.class);
        //intent.putExtra(getString(R.string.key_name), name);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                itemAdicionado = data.getExtras().getBoolean("booleanItem");
                listAdapter.notifyDataSetChanged();

            }
        }
    }

}


