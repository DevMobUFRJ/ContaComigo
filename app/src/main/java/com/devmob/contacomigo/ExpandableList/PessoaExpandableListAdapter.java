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
import com.devmob.contacomigo.model.ProdutoConsumido;

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
        List<ProdutoConsumido> produtos = pessoaList.get(indicePessoa).getProdutosConsumidos();
        return produtos.get(indiceProduto);
    }


    @Override
    public long getChildId(int indicePessoa, int indiceProduto) {
        return indiceProduto;
    }

    public void insereLista(Pessoa novo) {
        pessoaList.add(novo);
        this.notifyDataSetChanged();
    }

    public void deletaLista(Pessoa novo) {
        for (Pessoa p: pessoaList) {
            if (p.getId() == novo.getId()){
                pessoaList.remove(p);
                break;
            }
        }
        this.notifyDataSetChanged();
    }


    @Override
    public View getChildView(int indicePessoa, int indiceProduto, boolean isLastChild,
                             View view, ViewGroup parent) {

        Produto detailInfo = (Produto) getChild(indicePessoa, indiceProduto);
        Pessoa pessoa = (Pessoa)  getGroup(indicePessoa);

        double price = pessoa.getPrecoTotal();

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.list_de_pessoa_item_produto, null);
        }

        TextView produto = (TextView) view.findViewById(R.id.produto);
        produto.setText(detailInfo.getNome().trim());
        TextView produtoPreco = (TextView) view.findViewById(R.id.produtoPreco);
        if (ItemFragmento.gorjeta.getAtivo() == false)
            produtoPreco.setText(String.format("%.2f",price));
        else
            produtoPreco.setText(String.format("%.2f", price*ItemFragmento.gorjeta.getValor()));


        return view;
    }

    @Override
    public int getChildrenCount(int indicePessoa) {
        return pessoaList.get(indicePessoa).getProdutosConsumidos().size();
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
            view = inf.inflate(R.layout.list_de_pessoa_item_pessoa, null);
        }
        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(pessoa.getNome().trim());
        TextView pessoaPrice = (TextView) view.findViewById(R.id.pessoaPrice);
        pessoaPrice.setText(String.format("%.2f",pessoa.getPrecoTotal()));
        if (ItemFragmento.gorjeta.getAtivo() == false)
            pessoaPrice.setText(String.format("%.2f",pessoa.getPrecoTotal()));
        else
            pessoaPrice.setText(String.format("%.2f",pessoa.getPrecoTotal()*ItemFragmento.gorjeta.getValor()));


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