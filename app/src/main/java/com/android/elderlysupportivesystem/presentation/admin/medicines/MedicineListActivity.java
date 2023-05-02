package com.android.elderlysupportivesystem.presentation.admin.medicines;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Conflict;
import com.android.elderlysupportivesystem.data.models.Medicine;
import com.android.elderlysupportivesystem.datasource.SharedPreferencesDataSource;
import com.android.elderlysupportivesystem.di.ViewModelProviderFactory;
import com.android.elderlysupportivesystem.presentation.viewmodels.MedicinesViewModel;

import javax.inject.Inject;

public class MedicineListActivity extends DaggerAppCompatActivity implements MedicinesCallback {

    @BindView(R.id.medicine_list_recycler_view)
    RecyclerView medicinesRecyclerView;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    MedicinesViewModel medicinesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        ButterKnife.bind(this);

        medicinesViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(MedicinesViewModel.class);
        medicinesViewModel.retrieveMedicines();
        medicinesViewModel.observeMedicineListLiveData().observe(this, medicines -> {
            if (medicines != null && !medicines.isEmpty()) {
                MedicinesAdapter medicinesAdapter = new MedicinesAdapter(medicines, this);
                medicinesRecyclerView.setAdapter(medicinesAdapter);
            } else {
                Toast.makeText(this, "No current medicines", Toast.LENGTH_SHORT).show();
            }
        });

        medicinesViewModel.observeErrorState().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
    }

    @OnClick(R.id.add_medicine_fab)
    public void onAddMedicineClicked() {
        AddMedicineFragment addMedicineDialog = AddMedicineFragment.newInstance();
        addMedicineDialog.show(getSupportFragmentManager(), AddMedicineFragment.TAG);
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    public void onMedicineClicked(Medicine medicine) {
        MedicineConflictsFragment fragment = MedicineConflictsFragment.newInstance(medicine);
        fragment.show(getSupportFragmentManager(), MedicineConflictsFragment.TAG);
    }

    @Override
    public void onAddNewMedicine(Medicine medicine) {
        medicinesViewModel.addNewMedicine(medicine);
        medicinesViewModel.observeAddMedicineState().observe(this, success ->
                Toast.makeText(this, "Medicine is added successfully", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onAddMedicineConflict(Conflict conflict, String medicineId) {
        medicinesViewModel.addMedicineConflict(conflict, medicineId);
        medicinesViewModel.observeAddConflictState().observe(this, success ->
                Toast.makeText(this, "Conflict is added successfully", Toast.LENGTH_SHORT).show());
    }
}