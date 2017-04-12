package com.devmob.contacomigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.ProdutoDAO;
import com.devmob.contacomigo.model.Produto;

public class AddProdutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        final EditText nomeT = (EditText) findViewById(R.id.nome);
        final EditText precoT = (EditText) findViewById(R.id.preco);

        Button botaoSalvar = (Button) findViewById(R.id.salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeT.getText().toString();
                float preco = Float.parseFloat(precoT.getText().toString());
                Produto produto = new Produto(nome, preco);

                ProdutoDAO dao = new ProdutoDAO(AddProdutoActivity.this);
                dao.insere(produto);
                dao.close();

                Toast.makeText(AddProdutoActivity.this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddProdutoActivity.this, ItemsActivity.class);
                startActivity(intent);
            }
        });
    }
}
