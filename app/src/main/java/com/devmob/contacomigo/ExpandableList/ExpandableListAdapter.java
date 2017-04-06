package com.devmob.contacomigo.ExpandableList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.devmob.contacomigo.R;

import java.util.ArrayList;

/**
 * Created by DevMachine on 29/03/2017.
 */

// GROUP EH O PRODUTO, CHILD EH PESSOA

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ProdutoInfo> deptList;

    public ExpandableListAdapter(Context context, ArrayList<ProdutoInfo> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int indiceProduto, int indicePessoa) {
        ArrayList<PessoaInfo> productList = deptList.get(indiceProduto).getListProduto();
        return productList.get(indicePessoa);
    }

    @Override
    public long getChildId(int indiceProduto, int indicePessoa) {
        return indicePessoa;
    }


    @Override
    public View getChildView(int indiceProduto, int indicePessoa, boolean isLastChild,
                             View view, ViewGroup parent) {

        PessoaInfo detailInfo = (PessoaInfo) getChild(indiceProduto, indicePessoa);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_pessoa, null);
        }

        TextView pessoa = (TextView) view.findViewById(R.id.pessoa);
        pessoa.setText(detailInfo.getNomePessoa().trim());
        TextView pessoaPreco = (TextView) view.findViewById(R.id.pessoaPreco);
        pessoaPreco.setText(String.valueOf(detailInfo.getPreco()));

        return view;
    }

    @Override
    public int getChildrenCount(int indiceProduto) {

        ArrayList<PessoaInfo> listProduto = deptList.get(indiceProduto).getListProduto();
        return listProduto.size();

    }

    @Override
    public Object getGroup(int indiceProduto) {
        return deptList.get(indiceProduto);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int indiceProduto) {
        return indiceProduto;
    }

    @Override
    public View getGroupView(int indiceProduto, boolean isLastChild, View view,
                             ViewGroup parent) {

        ProdutoInfo produtoInfo = (ProdutoInfo) getGroup(indiceProduto);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.list_produto, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(produtoInfo.getNomeProduto().trim());
        TextView productPrice = (TextView) view.findViewById(R.id.productPrice);
        productPrice.setText(String.valueOf(produtoInfo.getProdutoPreco()));

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int indiceProduto, int indicePessoa) {
        return true;
    }
}