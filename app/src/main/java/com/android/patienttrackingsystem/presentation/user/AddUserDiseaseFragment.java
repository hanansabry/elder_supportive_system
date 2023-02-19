package com.android.patienttrackingsystem.presentation.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.di.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUserDiseaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUserDiseaseFragment extends BottomSheetDialogFragment {

    public static final String TAG = "AddUserDiseaseFragment.class";

    @BindView(R.id.diseases_spinner)
    Spinner diseasesSpinner;
    @BindView(R.id.status_spinner)
    Spinner statusSpinner;
    private MedicalProfileCallback medicalProfileCallback;
    private ArrayList<Disease> diseaseList;
    private ArrayList<String> statusList;

    public AddUserDiseaseFragment() {
        // Required empty public constructor
    }

    public static AddUserDiseaseFragment newInstance(ArrayList<Disease> diseaseList, ArrayList<String> statusList) {
        AddUserDiseaseFragment fragment = new AddUserDiseaseFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.DISEASE, diseaseList);
        args.putStringArrayList(Constants.STATUES, statusList);
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
            diseaseList = getArguments().getParcelableArrayList(Constants.DISEASE);
            statusList = getArguments().getStringArrayList(Constants.STATUES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user_disease, container, false);
        ButterKnife.bind(this, view);
        initiateDiseasesSpinner();
        return view;
    }

    private void initiateDiseasesSpinner() {
        ArrayAdapter<String> diseasesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        diseasesAdapter.add("Select Disease");
        for (Disease disease : diseaseList) {
            diseasesAdapter.add(disease.getName());
        }
        diseasesSpinner.setAdapter(diseasesAdapter);
    }

    @OnClick(R.id.save_disease)
    public void onSaveDiseaseClicked() {
        if (diseasesSpinner.getSelectedItemPosition() == 0 || statusSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "You must select disease and status", Toast.LENGTH_SHORT).show();
            return;
        }

        Disease selectedDisease = diseaseList.get(diseasesSpinner.getSelectedItemPosition()-1);
        String status = statusList.get(statusSpinner.getSelectedItemPosition());
        medicalProfileCallback.onAddNewUserDisease(selectedDisease, status);
        dismiss();
    }
}