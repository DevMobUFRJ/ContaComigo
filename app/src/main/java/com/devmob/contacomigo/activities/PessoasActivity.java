package com.devmob.contacomigo.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.devmob.contacomigo.R;

public class PessoasActivity extends AppCompatActivity {

    Intent intent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessoas);

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.moneyIcon:
                                //textFavorites.setVisibility(View.VISIBLE);
                                //textSchedules.setVisibility(View.GONE);
                                //item.setIcon(R.drawable.ic_people_black_48dp);
                                bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                                item.setChecked(true);
                                intent = new Intent(PessoasActivity.this, ItemsActivity.class);
                                startActivityForResult(intent, 2);
                                Toast.makeText(PessoasActivity.this, "Money", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.personIcon:
                                bottomNavigationView.setItemBackgroundResource(R.color.red);
                                item.setChecked(true);
                                Toast.makeText(PessoasActivity.this, "Person", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tempIcon:
                                bottomNavigationView.setItemBackgroundResource(R.color.black);
                                item.setChecked(true);
                                Toast.makeText(PessoasActivity.this, "tempIcon", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });

    }
}
