package com.android.patienttrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.patienttrackingsystem.presentation.SignInActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, SignInActivity.class));
        }, SPLASH_TIME_OUT);
    }
}