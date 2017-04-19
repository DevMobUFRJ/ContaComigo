package com.devmob.contacomigo.ExpandableList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.activities.ItemsActivity;
import com.devmob.contacomigo.model.Pessoa;
import com.devmob.contacomigo.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DevMachine on 29/03/2017.
 */

// GROUP EH O PRODUTO, CHILD EH PESSOA

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Produto> deptList;

    public ExpandableListAdapter(Context context, ArrayList<Produto> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int indiceProduto, int indicePessoa) {
        List<Pessoa> consumidores = deptList.get(indiceProduto).getConsumidores();
        return consumidores.get(indicePessoa);
    }

    @Override
    public long getChildId(int indiceProduto, int indicePessoa) {
        return indicePessoa;
    }


    @Override
    public View getChildView(int indiceProduto, int indicePessoa, boolean isLastChild,
                             View view, ViewGroup parent) {

        Pessoa detailInfo = (Pessoa) getChild(indiceProduto, indicePessoa);
        Produto produto = (Produto) getGroup(indiceProduto);

        double price = produto.getPreco()/produto.getConsumidores().size();
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_pessoa, null);
        }

        TextView pessoa = (TextView) view.findViewById(R.id.pessoa);
        pessoa.setText(detailInfo.getNome().trim());
        TextView pessoaPreco = (TextView) view.findViewById(R.id.pessoaPreco);
        if (ItemsActivity.gorjeta.getAtivo() == false)
            pessoaPreco.setText(String.format("%.2f",price));
        else
            pessoaPreco.setText(String.format("%.2f", price*ItemsActivity.gorjeta.getValor()));

        return view;
    }

    @Override
    public int getChildrenCount(int indiceProduto) {
        return deptList.get(indiceProduto).getConsumidores().size();
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

        Produto produto = (Produto) getGroup(indiceProduto);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.list_produto, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(produto.getNome().trim());
        TextView productPrice = (TextView) view.findViewById(R.id.productPrice);
        productPrice.setText(String.format("%.2f",produto.getPreco()));
        if (ItemsActivity.gorjeta.getAtivo() == false)
            productPrice.setText(String.format("%.2f",produto.getPreco()));
        else
            productPrice.setText(String.format("%.2f",produto.getPreco()*ItemsActivity.gorjeta.getValor()));

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