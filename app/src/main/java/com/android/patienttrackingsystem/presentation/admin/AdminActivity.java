package com.android.patienttrackingsystem.presentation.admin;

import android.content.Intent;
import android.os.Bundle;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.presentation.admin.diseases.DiseaseListActivity;
import com.android.patienttrackingsystem.presentation.admin.medicines.MedicineListActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminActivity extends AppCompatActivity {

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

    @OnClick(R.id.back_button)
    public void onBackCLicked() {
        onBackPressed();
    }
}