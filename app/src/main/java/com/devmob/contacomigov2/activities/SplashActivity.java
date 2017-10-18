package com.devmob.contacomigov2.activities;

/**
 * Created by DevMobUFRJ on 18/10/2017.
 */

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.devmob.contacomigov2.R;


public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView imageView = (ImageView) findViewById(R.id.logoDevmob);
        final Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.alpha);
        imageView.startAnimation(animation);
        animation.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        ActivityOptionsCompat opts = ActivityOptionsCompat.makeCustomAnimation(SplashActivity.this, R.anim.alpha,R.anim.reverse_alpha);
        ActivityCompat.startActivity(this,intent,opts.toBundle());
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}