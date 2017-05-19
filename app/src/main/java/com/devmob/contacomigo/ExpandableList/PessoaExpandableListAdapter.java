package com.devmob.contacomigo.ExpandableList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.fragments.ItemFragmento;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DevMob on 05/05/2017.
 */

public class PessoaExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Pessoa> pessoaList;

    public PessoaExpandableListAdapter(Context context, ArrayList<Pessoa> pessoaList) {
        this.context = context;
        this.pessoaList = pessoaList;
    }

    @Override
    public Object getChild(int indicePessoa, int indiceProduto) {
        List<Produto> produtos = pessoaList.get(indiceProduto).getProdutos();
        return produtos.get(indicePessoa);
    }

    public void insereLista(Pessoa novo) {
        pessoaList.add(novo);
        this.notifyDataSetChanged();
    }

    @Override
    public long getChildId(int indicePessoa, int indiceProduto) {
        return indicePessoa;
    }


    @Override
    public View getChildView(int indicePessoa, int indiceProduto, boolean isLastChild,
                             View view, ViewGroup parent) {

        Produto detailInfo = (Produto) getChild(indicePessoa, indiceProduto);
        Pessoa pessoa = (Pessoa)  getGroup(indicePessoa);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listpessoa_produto, null);
        }

        return view;
    }

    @Override
    public int getChildrenCount(int indicePessoa) {
        return pessoaList.get(indicePessoa).getProdutos().size();
    }

    @Override
    public Object getGroup(int indicePessoa) {
        return pessoaList.get(indicePessoa);
    }

    @Override
    public int getGroupCount() {
        return pessoaList.size();
    }

    @Override
    public long getGroupId(int indicePessoa) {
        return indicePessoa;
    }

    @Override
    public View getGroupView(int indicePessoa, boolean isLastChild, View view,
                             ViewGroup parent) {

        Pessoa pessoa = (Pessoa) getGroup(indicePessoa);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.listpessoa_pessoa, null);
        }

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int indicePessoa, int indiceProduto) {
        return true;
    }
}