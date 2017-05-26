package com.devmob.contacomigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.fragments.ItemFragmento;
import com.devmob.contacomigo.fragments.NonSwipeableViewPager;
import com.devmob.contacomigo.fragments.PessoaFragmento;
import com.devmob.contacomigo.fragments.RestauranteFragmento;
import com.devmob.contacomigo.fragments.SectionsStatePagerAdapter;
import com.devmob.contacomigo.fragments.TotalFragmento;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devmob on 03/05/17.
 */



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RESTAURANTEFRAG = 0;
    private static final int ITEMFRAG = 1;
    private static final int PESSOAFRAG = 2;
    private static final int TOTALFRAG = 3;


    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private NonSwipeableViewPager mViewPager;
    //TESTE
    public static BottomSheetBehavior mBottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");

        //BottomMenu
        View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setPeekHeight(0);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });
        //Fim BottomMenu

        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = (NonSwipeableViewPager) findViewById(R.id.containter);
        //setup the pager
        setupViewPager(mViewPager);



        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.restIcon:
                                item.setChecked(true);
                                setViewPager(RESTAURANTEFRAG);
                                break;
                            case R.id.moneyIcon:
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                                        .show( fragmento )
                                        .commit();
                                break;
                            case R.id.personIcon:
                                item.setChecked(true);
                                setViewPager(PESSOAFRAG);
                                break;
                            case R.id.tempIcon:
                                //bottomNavigationView.setItemBackgroundResource(R.color.black);
                                item.setChecked(true);
                                setViewPager(TOTALFRAG);
                                break;
                        }
                        return false;
                    }
                });



    }

    private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        List<Fragment> fragmentos = new ArrayList<>();
        fragmentos.add(new RestauranteFragmento());
        fragmentos.add(new ItemFragmento());
        fragmentos.add(new PessoaFragmento());
        fragmentos.add(new TotalFragmento());
        for(Fragment fragmento : fragmentos){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .show( fragmento )
                    .commit();
        }

        adapter.addFragment(new RestauranteFragmento(), "Restaurante");
        adapter.addFragment(new ItemFragmento(), "Item");
        adapter.addFragment(new PessoaFragmento(), "Pessoa");
        adapter.addFragment(new TotalFragmento(), "Total");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }
}