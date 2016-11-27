package com.gopal.itunesmusicplayer.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gopal.itunesmusicplayer.R;

public class SplashScreenActivity extends AppCompatActivity {

    private final static int TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Animation animFadIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        ImageView ivIcon = (ImageView) findViewById(R.id.iv_icon);
        final EditText etSearch = (EditText) findViewById(R.id.et_search);
        TextView tvDesc = (TextView) findViewById(R.id.tv_description);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                etSearch.setVisibility(View.VISIBLE);
                etSearch.setAnimation(animFadIn);
            }
        }, TIME_OUT);
    }

    public void goToSearchScreen(View v){
        Intent intentSearchScreen = new Intent(SplashScreenActivity.this,SearchScreenActivity.class);
        startActivity(intentSearchScreen);
    }
}
