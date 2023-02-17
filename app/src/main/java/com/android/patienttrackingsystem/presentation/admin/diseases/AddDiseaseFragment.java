package com.android.patienttrackingsystem.presentation.admin.diseases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.patienttrackingsystem.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDiseaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDiseaseFragment extends BottomSheetDialogFragment {

    public static final String TAG = "AddDiseaseFragment.class";

    public AddDiseaseFragment() {
        // Required empty public constructor
    }

    public static AddDiseaseFragment newInstance() {
        return new AddDiseaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_disease, container, false);
    }
}