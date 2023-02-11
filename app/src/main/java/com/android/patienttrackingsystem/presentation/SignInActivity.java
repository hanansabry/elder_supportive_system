package com.android.patienttrackingsystem.presentation;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;

import com.android.patienttrackingsystem.R;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sign_in)
    public void onSignInClicked() {

    }

    @OnClick(R.id.sign_up)
    public void onSignUpClicked() {

    }

    @OnClick(R.id.login_as_admin)
    public void onLoginAsAdminClicked() {
        startActivity(new Intent(this, AdminActivity.class));
    }

    @OnClick(R.id.scan_qr_code)
    public void onScanQrCodeClicked() {

    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}