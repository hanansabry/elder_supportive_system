package com.android.patienttrackingsystem.presentation.admin.medicines;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;

import com.android.patienttrackingsystem.R;

public class MedicineListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_medicine_fab)
    public void onAddMedicineClicked() {
//        AddMedicineFragment addMedicineDialog = AddMedicineFragment.newInstance();
//        addMedicineDialog.show(getSupportFragmentManager(), AddMedicineFragment.TAG);
        MedicineConflictsFragment fragment = MedicineConflictsFragment.newInstance();
        fragment.show(getSupportFragmentManager(), MedicineConflictsFragment.TAG);
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}