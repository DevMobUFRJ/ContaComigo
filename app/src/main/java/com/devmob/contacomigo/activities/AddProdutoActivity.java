package com.devmob.contacomigo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.model.Produto;

public class AddProdutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        EditText nome = (EditText) findViewById(R.id.nome);
        EditText preco = (EditText) findViewById(R.id.preco);


        Button botaoSalvar = (Button) findViewById(R.id.salvar);
        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Produto produto = new Produto();
                //produto.setNome(nome.getText());
                Toast.makeText(AddProdutoActivity.this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
