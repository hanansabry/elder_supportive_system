package com.android.patienttrackingsystem.presentation.admin.diseases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.presentation.admin.medicines.MedicinesCallback;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDiseaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDiseaseFragment extends BottomSheetDialogFragment {

    public static final String TAG = "AddDiseaseFragment.class";

    @BindView(R.id.disease_name_edit_text)
    EditText diseaseNameEditText;
    @BindView(R.id.disease_desc_edit_text)
    EditText diseaseDescEditText;
    private DiseasesCallback diseaseCallback;

    public AddDiseaseFragment() {
        // Required empty public constructor
    }

    public static AddDiseaseFragment newInstance() {
        return new AddDiseaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof DiseasesCallback) {
            diseaseCallback = (DiseasesCallback) getContext();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_disease, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.save_disease)
    public void onSaveDiseaseClicked() {
        String name = diseaseNameEditText.getText().toString();
        String desc = diseaseDescEditText.getText().toString();

        if (name.isEmpty() || desc.isEmpty()) {
            Toast.makeText(getContext(), "You must enter name and description", Toast.LENGTH_SHORT).show();
        } else {
            Disease disease = new Disease();
            disease.setName(name);
            disease.setDesc(desc);
            diseaseCallback.onAddNewDisease(disease);
            dismiss();
        }
    }
}