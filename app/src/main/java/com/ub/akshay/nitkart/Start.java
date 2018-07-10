package com.ub.akshay.nitkart;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;


public class Start extends AppCompatActivity {
    ProgressBar progressBar;
    private static final String TAG = Start.class.getSimpleName();
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#1ab78d"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
                Intent intent = new Intent(Start.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

        }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

