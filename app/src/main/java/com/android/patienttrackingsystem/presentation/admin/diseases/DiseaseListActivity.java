package com.android.patienttrackingsystem.presentation.admin.diseases;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;

import com.android.patienttrackingsystem.R;

public class DiseaseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_list);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_disease_fab)
    public void onAddDiseaseClicked() {
        DiseaseConflictsFragment fragment = DiseaseConflictsFragment.newInstance();
        fragment.show(getSupportFragmentManager(), DiseaseConflictsFragment.TAG);
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}