package com.devmob.contacomigo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.dao.PessoaDAO;
import com.devmob.contacomigo.fragments.FragmentInterface;
import com.devmob.contacomigo.fragments.ItemFragmento;
import com.devmob.contacomigo.fragments.NonSwipeableViewPager;
import com.devmob.contacomigo.fragments.PessoaFragmento;
import com.devmob.contacomigo.fragments.RestauranteFragmento;
import com.devmob.contacomigo.fragments.SectionsStatePagerAdapter;
import com.devmob.contacomigo.fragments.TotalFragmento;
import com.devmob.contacomigo.model.Pessoa;

/**
 * Created by devmob on 03/05/17.
 */



public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private static final int RESTAURANTEFRAG = 0;
    public static final RestauranteFragmento restauranteFragmento = new RestauranteFragmento();
    private static final int ITEMFRAG = 2;
    public static final ItemFragmento itemFragmento = new ItemFragmento();
    private static final int PESSOAFRAG = 1;
    public static final PessoaFragmento pessoaFragmento = new PessoaFragmento();
    private static final int TOTALFRAG = 3;
    public static final TotalFragmento totalFragmento = new TotalFragmento();

    private SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    private NonSwipeableViewPager mViewPager;
    //TESTE
    public static BottomSheetBehavior mBottomSheetBehavior;

    public static void forceReloadAllFragments(){
        itemFragmento.forceReload();
        pessoaFragmento.forceReload();
        totalFragmento.forceReload();
        restauranteFragmento.forceReload();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Started.");

        PessoaDAO dao = new PessoaDAO(this);
        if (dao.isEmpty()){
            Pessoa p = new Pessoa(getResources().getString(R.string.default_person));
            dao.insere(p);
            Log.d(TAG, "onCreate: Tabela vazia");
        }
        
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
        mViewPager.setOffscreenPageLimit(4);
        //setup the pager
        setupViewPager(mViewPager);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float v, final int i2) {
            }

            @Override
            public void onPageSelected(final int position) {
                FragmentInterface fragment = (FragmentInterface) mSectionsStatePagerAdapter.instantiateItem(mViewPager, position);
                Log.d(TAG, "Fragment Selected: "+position);
                Log.d(TAG, "Fragment is: "+fragment);
                if (fragment != null) {
                    fragment.fragmentBecameVisible();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int position) {
            }
        });

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if(position <= -1.0F || position >= 1.0F) {
                    page.setTranslationX(page.getWidth() * position);
                    page.setAlpha(0.0F);
                } else if( position == 0.0F ) {
                    page.setTranslationX(page.getWidth() * position);
                    page.setAlpha(1.0F);
                } else {
                    // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                    page.setTranslationX(page.getWidth() * -position);
                    page.setAlpha(1.0F - Math.abs(position));
                }
            }
        });


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
                                //textFavorites.setVisibility(View.VISIBLE);
                                //textSchedules.setVisibility(View.GONE);
                                //item.setIcon(R.drawable.ic_people_black_48dp);
                                item.setChecked(true);
                                setViewPager(ITEMFRAG);
                                //Toast.makeText(ItemsActivity.this, "Money", Toast.LENGTH_SHORT).show();
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
        //SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        mSectionsStatePagerAdapter.addFragment(restauranteFragmento, "RestauranteFragmento");
        mSectionsStatePagerAdapter.addFragment(pessoaFragmento, "PessoaFragmento");
        mSectionsStatePagerAdapter.addFragment(itemFragmento, "ItemFragmento");
        mSectionsStatePagerAdapter.addFragment(totalFragmento, "TotalFragmento");
        viewPager.setAdapter(mSectionsStatePagerAdapter);
    }

    public void setViewPager(int fragmentNumber){
        mViewPager.setCurrentItem(fragmentNumber);
    }


}