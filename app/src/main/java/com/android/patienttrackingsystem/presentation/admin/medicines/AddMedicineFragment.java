package com.android.patienttrackingsystem.presentation.admin.medicines;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.Medicine;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMedicineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMedicineFragment extends BottomSheetDialogFragment {

    public static final String TAG = "AddMedicineFragment.class";
    private MedicinesCallback medicinesCallback;

    @BindView(R.id.medicine_name_edit_text)
    EditText medicineNameEditText;
    @BindView(R.id.medicine_desc_edit_text)
    EditText medicineDescEditText;

    public AddMedicineFragment() {
        // Required empty public constructor
    }

    public static AddMedicineFragment newInstance() {
        return new AddMedicineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof MedicinesCallback) {
            medicinesCallback = (MedicinesCallback) getContext();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_medicine, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.save_medicine)
    public void onSaveMedicineClicked() {
        String name = medicineNameEditText.getText().toString();
        String desc = medicineDescEditText.getText().toString();

        if (name.isEmpty() || desc.isEmpty()) {
            Toast.makeText(getContext(), "You must enter name and description", Toast.LENGTH_SHORT).show();
        } else {
            Medicine medicine = new Medicine();
            medicine.setName(name);
            medicine.setDesc(desc);
            medicinesCallback.onAddNewMedicine(medicine);
            dismiss();
        }
    }
}