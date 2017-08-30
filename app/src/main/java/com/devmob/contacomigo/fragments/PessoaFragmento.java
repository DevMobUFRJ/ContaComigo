package com.devmob.contacomigo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devmob.contacomigo.ExpandableList.PessoaExpandableListAdapter;
import com.devmob.contacomigo.R;
import com.devmob.contacomigo.activities.AddPessoaActivity;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.model.Gorjeta;
import com.devmob.contacomigo.model.Pessoa;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by devmob on 03/05/17.
 */

public class PessoaFragmento extends Fragment implements FragmentInterface{
    private static final String TAG = "PessoaFragmento";


    public static PessoaExpandableListAdapter listAdapter;
    private ExpandableListView pessoasExpandableListView;
    public FloatingActionButton addFAB;
    public Button apagaTudo;
    public static boolean itemAdicionado;
    public static Gorjeta gorjeta;
    public static SwitchCompat switchGorjeta;
    public static Spinner gorjetaSpinner;
    private String nomeFragmento = "Pessoa";
    List<Pessoa> pessoas;
    private boolean atualizar = true;

    public String getNome(){
        return nomeFragmento;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pessoas_layout, container, false);

        //Inicialização
        gorjetaSpinner = (Spinner) view.findViewById(R.id.gorjetaSpinner);
        apagaTudo = (Button) view.findViewById(R.id.deletaTudo);
        switchGorjeta = (SwitchCompat) view.findViewById(R.id.switchGorjeta);

        pessoasExpandableListView = (ExpandableListView) view.findViewById(R.id.pessoasExpandableListView);
        PessoaDAO dao = new PessoaDAO(getActivity());
        pessoas = dao.buscaPessoas();
        dao.close();

        SharedPreferences prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gorjetas, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gorjetaSpinner.setAdapter(adapter);
        gorjetaSpinner.setOnItemSelectedListener(gorjetaSpinnerListener);
        gorjetaSpinner.setSelection(prefs.getInt("gorjetaValor", 9));

        gorjeta = new Gorjeta(prefs.getInt("gorjetaPorcentagem", 10), prefs.getBoolean("gorjetaAtivo", false));
        switchGorjeta.setChecked(prefs.getBoolean("switchGorjeta", false));

        listAdapter = new PessoaExpandableListAdapter(getActivity(), new ArrayList<>(pessoas));
        pessoasExpandableListView.setAdapter(listAdapter);
        addFAB = (FloatingActionButton) view.findViewById(R.id.addFAB);

        //Seta tamanho máximo do Dropdown do Spinner
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(gorjetaSpinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        //LONG CLICK EM CADA CHILD (PESSOA E PREÇO)
        pessoasExpandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int indicePessoa = ExpandableListView.getPackedPositionGroup(id);
                int indiceProduto = ExpandableListView.getPackedPositionChild(id);
                //get the group header
                PessoaDAO dao = new PessoaDAO(getActivity());
                List<Pessoa> pessoas = dao.buscaPessoas();
                dao.close();
                List<Pessoa> listPessoas = new ArrayList<Pessoa>(pessoas);
                Pessoa pessoa = listPessoas.get(indicePessoa);
                //LONG CLICK NO PRODUTO
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    //Produto produto = pessoa.getProdutosConsumidos().get(indiceProduto);
                    //Toast.makeText(getActivity(), pessoa.getNome() + "/" + indicePessoa + " deve " + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                //LONG CLICK NA PESSOA
                else if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    //Toast.makeText(getActivity(), produto.getNome() + " " + produto.getPreco(), Toast.LENGTH_SHORT).show();
                    BottomSheetMenuPessoa bottomSheetDialogFragment = new BottomSheetMenuPessoa();
                    bottomSheetDialogFragment.setPessoaFragmento(PessoaFragmento.this);
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
                //Produto produto = pessoa.getProdutosConsumidos().get(indiceProduto);
                //Toast.makeText(getActivity(), " Clicked on :: " + pessoa.getNome() + "/" + indiceProduto + "/" + pessoa.getPrecoTotal(), Toast.LENGTH_SHORT).show();
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
                    Log.d(TAG, "onCheckedChanged: FALSEI PESSOA");
                    ((TextView)gorjetaSpinner.getChildAt(0)).setTextColor(Color.BLACK);
                    gorjeta.setAtivo(false);
                    ((TextView)ItemFragmento.gorjetaSpinner.getChildAt(0)).setTextColor(Color.BLACK);
                    ItemFragmento.gorjeta.setAtivo(false);
                    ItemFragmento.switchGorjeta.setChecked(false);
                    listAdapter.notifyDataSetChanged();
                }
                else {
                            Log.d(TAG, "onCheckedChanged: AGORA É TRUE PESSOA");
                            ((TextView)gorjetaSpinner.getChildAt(0)).setTextColor(Color.RED);
                            gorjeta.setAtivo(true);
                             ((TextView)ItemFragmento.gorjetaSpinner.getChildAt(0)).setTextColor(Color.RED);
                            ItemFragmento.gorjeta.setAtivo(true);
                            ItemFragmento.switchGorjeta.setChecked(true);
                            listAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }


    public void atualizaListas(){
        PessoaDAO pdao = new PessoaDAO(getActivity());
        pessoas = pdao.buscaPessoas();
    }


    public void onResume() {
        super.onResume();
        PessoaDAO pdao = new PessoaDAO(getContext());
        pessoas = pdao.buscaPessoas();

        if (itemAdicionado == true) {

            //pega consumidores relacionados a esse produto
            PessoaDAO pedao = new PessoaDAO(getActivity());
            pessoas = pedao.buscaPessoas();

            listAdapter.resetaLista(pessoas);
            listAdapter.notifyDataSetChanged();
            itemAdicionado = false;
        }
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

    @Override
    public void onPause() {
        super.onPause();
    }

    private void telaAdicionar() {
        Intent intent = new Intent(getActivity(), AddPessoaActivity.class);
        //intent.putExtra(getString(R.string.key_name), name);
        startActivityForResult(intent, 2);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                itemAdicionado = data.getExtras().getBoolean("booleanItem");
                listAdapter.notifyDataSetChanged();

            }
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
            ItemFragmento.gorjeta.setPorcentagem(Integer.parseInt(parent.getItemAtPosition(position).toString()));
            ItemFragmento.gorjetaSpinner.setSelection(position);
            listAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    @Override
    public void setAtualizar(boolean b) {
        this.atualizar = b;
    }

    @Override
    public void fragmentBecameVisible() {
        if(atualizar){
            PessoaDAO pdao = new PessoaDAO(getContext());
            pessoas = pdao.buscaPessoas();
            listAdapter.resetaLista(pessoas);
            listAdapter.notifyDataSetChanged();
            atualizar = false;
            Log.i(TAG, "PessoaFrag Atualizada");
        }
    }
}


