package com.android.elderlysupportivesystem.presentation.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Medicine;
import com.android.elderlysupportivesystem.di.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUserMedicineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUserMedicineFragment extends BottomSheetDialogFragment {

    public static final String TAG = "AddUserMedicineFragment.class";

    @BindView(R.id.medicines_spinner)
    Spinner medicinesSpinner;
    @BindView(R.id.medicine_notes)
    EditText notesEditText;
    private MedicalProfileCallback medicalProfileCallback;
    private ArrayList<Medicine> medicineList;

    public AddUserMedicineFragment() {
        // Required empty public constructor
    }

    public static AddUserMedicineFragment newInstance(ArrayList<Medicine> medicineList) {
        AddUserMedicineFragment fragment = new AddUserMedicineFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.MEDICINE, medicineList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof MedicalProfileCallback) {
            medicalProfileCallback = (MedicalProfileCallback) getContext();
        }
        if (getArguments() != null) {
            medicineList = getArguments().getParcelableArrayList(Constants.MEDICINE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user_medicine, container, false);
        ButterKnife.bind(this, view);
        initiateMedicinesSpinner();
        return view;
    }

    private void initiateMedicinesSpinner() {
        ArrayAdapter<String> medicinesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        medicinesAdapter.add("Select Medicine");
        for (Medicine medicine : medicineList) {
            medicinesAdapter.add(medicine.getName());
        }
        medicinesSpinner.setAdapter(medicinesAdapter);
    }

    @OnClick(R.id.save_medicine)
    public void onSaveMedicineClicked() {
        String notes = notesEditText.getText().toString();
        if (medicinesSpinner.getSelectedItemPosition() == 0 || notes.isEmpty()) {
            Toast.makeText(getContext(), "You must select medicine and notes", Toast.LENGTH_SHORT).show();
            return;
        }

        Medicine selectedMedicine = medicineList.get(medicinesSpinner.getSelectedItemPosition()-1);
        medicalProfileCallback.onAddNewUserMedicine(selectedMedicine, notes);
        dismiss();
    }
}