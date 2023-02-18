package com.android.patienttrackingsystem.presentation.admin;

import android.content.Intent;
import android.os.Bundle;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.datasource.SharedPreferencesDataSource;
import com.android.patienttrackingsystem.presentation.SignInActivity;
import com.android.patienttrackingsystem.presentation.admin.diseases.DiseaseListActivity;
import com.android.patienttrackingsystem.presentation.admin.medicines.MedicineListActivity;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class AdminActivity extends DaggerAppCompatActivity {

    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.medicines_card_view, R.id.add_medicine})
    public void onMedicinesClicked() {
        startActivity(new Intent(this, MedicineListActivity.class));
    }

    @OnClick({R.id.diseases_card_view, R.id.add_disease})
    public void onDiseasesClicked() {
        startActivity(new Intent(this, DiseaseListActivity.class));
    }

    @OnClick(R.id.sign_out)
    public void onSignOut() {
        FirebaseAuth.getInstance().signOut();
        sharedPreferencesDataSource.removeAllValues();
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.back_button)
    public void onBackCLicked() {
        onBackPressed();
    }
}