package com.android.elderlysupportivesystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.elderlysupportivesystem.di.Constants;
import com.android.elderlysupportivesystem.presentation.SignInActivity;
import com.android.elderlysupportivesystem.presentation.admin.AdminActivity;
import com.android.elderlysupportivesystem.presentation.user.MedicalProfileActivity;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
            boolean isAdmin = sharedPreferences.getBoolean(Constants.IS_ADMIN, false);
            String userId = sharedPreferences.getString(Constants.USER_ID, null);
            if (userId == null) {
                startActivity(new Intent(this, SignInActivity.class));
            } else {
                //check if user is admin or not
                if (isAdmin) {
                    startActivity(new Intent(this, AdminActivity.class));
                } else {
                    startActivity(new Intent(this, MedicalProfileActivity.class));
                }
            }
        }, SPLASH_TIME_OUT);
    }
}