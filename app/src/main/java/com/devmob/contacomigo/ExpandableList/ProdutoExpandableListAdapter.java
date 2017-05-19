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
 * Created by DevMachine on 29/03/2017.
 */

// GROUP EH O PRODUTO, CHILD EH PESSOA

public class ProdutoExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Produto> prodList;

    public ProdutoExpandableListAdapter(Context context, ArrayList<Produto> prodList) {
        this.context = context;
        this.prodList = prodList;
    }

    @Override
    public Object getChild(int indiceProduto, int indicePessoa) {
        List<Pessoa> consumidores = prodList.get(indiceProduto).getConsumidores();
        return consumidores.get(indicePessoa);
    }

    public void insereLista(Produto novo) {
        prodList.add(novo);
        this.notifyDataSetChanged();
    }

    public void deletaLista(Produto novo) {
        for (Produto p: prodList) {
            if (p.getId() == novo.getId()){
                prodList.remove(p);
                break;
            }
        }
        this.notifyDataSetChanged();
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
            view = infalInflater.inflate(R.layout.list_de_produto_item_pessoa, null);
        }

        TextView pessoa = (TextView) view.findViewById(R.id.pessoa);
        pessoa.setText(detailInfo.getNome().trim());
        TextView pessoaPreco = (TextView) view.findViewById(R.id.pessoaPreco);
        if (ItemFragmento.gorjeta.getAtivo() == false)
            pessoaPreco.setText(String.format("%.2f",price));
        else
            pessoaPreco.setText(String.format("%.2f", price*ItemFragmento.gorjeta.getValor()));

        return view;
    }

    @Override
    public int getChildrenCount(int indiceProduto) {
        return prodList.get(indiceProduto).getConsumidores().size();
    }

    @Override
    public Object getGroup(int indiceProduto) {
        return prodList.get(indiceProduto);
    }

    @Override
    public int getGroupCount() {
        return prodList.size();
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
            view = inf.inflate(R.layout.list_de_produto_item_produto, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(produto.getNome().trim());
        TextView productPrice = (TextView) view.findViewById(R.id.productPrice);
        productPrice.setText(String.format("%.2f",produto.getPreco()));
        if (ItemFragmento.gorjeta.getAtivo() == false)
            productPrice.setText(String.format("%.2f",produto.getPreco()));
        else
            productPrice.setText(String.format("%.2f",produto.getPreco()*ItemFragmento.gorjeta.getValor()));

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