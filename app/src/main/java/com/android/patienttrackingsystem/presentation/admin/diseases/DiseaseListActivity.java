package com.android.patienttrackingsystem.presentation.admin.diseases;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.di.ViewModelProviderFactory;
import com.android.patienttrackingsystem.presentation.admin.medicines.MedicineConflictsFragment;
import com.android.patienttrackingsystem.presentation.admin.medicines.MedicinesAdapter;
import com.android.patienttrackingsystem.presentation.viewmodels.DiseasesViewModel;
import com.android.patienttrackingsystem.presentation.viewmodels.MedicinesViewModel;

import javax.inject.Inject;

public class DiseaseListActivity extends DaggerAppCompatActivity implements DiseasesCallback{

    @BindView(R.id.disease_list_recycler_view)
    RecyclerView diseaseRecyclerView;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    DiseasesViewModel diseasesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_list);
        ButterKnife.bind(this);

        diseasesViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(DiseasesViewModel.class);
        diseasesViewModel.retrieveDiseases();
        diseasesViewModel.observeDiseaseListLiveData().observe(this, diseases -> {
            if (diseases != null && !diseases.isEmpty()) {
                DiseasesAdapter diseasesAdapter = new DiseasesAdapter(diseases, this);
                diseaseRecyclerView.setAdapter(diseasesAdapter);
            } else {
                Toast.makeText(this, "No current diseases", Toast.LENGTH_SHORT).show();
            }
        });

        diseasesViewModel.observeErrorState().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
    }

    @OnClick(R.id.add_disease_fab)
    public void onAddDiseaseClicked() {
        AddDiseaseFragment fragment = AddDiseaseFragment.newInstance();
        fragment.show(getSupportFragmentManager(), AddDiseaseFragment.TAG);
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onDiseaseClicked(Disease disease) {
        DiseaseConflictsFragment fragment = DiseaseConflictsFragment.newInstance(disease);
        fragment.show(getSupportFragmentManager(), DiseaseConflictsFragment.TAG);
    }

    @Override
    public void onAddNewDisease(Disease disease) {
        diseasesViewModel.addNewDisease(disease);
        diseasesViewModel.observeAddDiseaseState().observe(this, success ->
                Toast.makeText(this, "Disease is added successfully", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAddDiseaseConflict(Conflict conflict, String diseaseId) {
        diseasesViewModel.addDiseaseConflict(conflict, diseaseId);
        diseasesViewModel.observeAddConflictState().observe(this, success ->
                Toast.makeText(this, "Conflict is added successfully", Toast.LENGTH_SHORT).show());
    }
}