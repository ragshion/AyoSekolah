package com.ragshion.ayosekolah.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ragshion.ayosekolah.R;


public class SplashscreenActivity extends AppCompatActivity {

    MaterialDialog materialDialog;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashscreenActivity.this, MenuUtamaActivity.class);
            SplashscreenActivity.this.startActivity(intent);
            SplashscreenActivity.this.finish();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }



}

