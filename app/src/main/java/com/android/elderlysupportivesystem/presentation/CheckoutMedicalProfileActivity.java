package com.android.elderlysupportivesystem.presentation;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Disease;
import com.android.elderlysupportivesystem.data.models.Medicine;
import com.android.elderlysupportivesystem.datasource.SharedPreferencesDataSource;
import com.android.elderlysupportivesystem.di.Constants;
import com.android.elderlysupportivesystem.di.ViewModelProviderFactory;
import com.android.elderlysupportivesystem.presentation.user.UserDiseasesAdapter;
import com.android.elderlysupportivesystem.presentation.user.UserMedicinesAdapter;
import com.android.elderlysupportivesystem.presentation.viewmodels.MedicalProfileViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

public class CheckoutMedicalProfileActivity extends DaggerAppCompatActivity {

    private String userId;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.civil_id)
    TextView civilId;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.blood_type)
    TextView bloodType;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.user_diseases_recyclerview)
    RecyclerView userDiseasesRecyclerView;
    @BindView(R.id.diseases_empty_view)
    View diseasesEmptyView;
    @BindView(R.id.user_medicines_recyclerview)
    RecyclerView userMedicinesRecyclerView;
    @BindView(R.id.medicines_empty_view)
    View medicineEmptyView;
    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    MedicalProfileViewModel medicalProfileViewModel;
    private ArrayList<Disease> allDiseasesList;
    private ArrayList<Medicine> allMedicineList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_medical_profile);
        ButterKnife.bind(this);

        String userId = getIntent().getStringExtra(Constants.USER_ID);
        medicalProfileViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(MedicalProfileViewModel.class);
        medicalProfileViewModel.retrieveUserData(userId);
        medicalProfileViewModel.retrieveUserDiseases(userId);
        medicalProfileViewModel.retrieveUserMedicines(userId);
        observeUserData();
        observeRetrieveUserDiseases();
        observeRetrieveUserMedicines();
    }

    private void observeUserData() {
        medicalProfileViewModel.observeUserState().observe(this, user -> {
            userName.setText(user.getUserName());
            civilId.setText(user.getCivilId());
            phone.setText(user.getPhone());
            address.setText(user.getAddress());
            bloodType.setText(user.getBloodType());
            age.setText(user.getAge());
        });
    }

    private void observeRetrieveUserDiseases() {
        medicalProfileViewModel.observeUserDiseaseListLiveData().observe(this, userDiseasesMap -> {
            if (userDiseasesMap == null || userDiseasesMap.isEmpty()) {
                diseasesEmptyView.setVisibility(View.VISIBLE);
                userDiseasesRecyclerView.setVisibility(View.GONE);
            } else {
                diseasesEmptyView.setVisibility(View.GONE);
                userDiseasesRecyclerView.setVisibility(View.VISIBLE);

                UserDiseasesAdapter userDiseasesAdapter = new UserDiseasesAdapter(userDiseasesMap);
                userDiseasesRecyclerView.setAdapter(userDiseasesAdapter);
            }
        });
    }

    private void observeRetrieveUserMedicines() {
        medicalProfileViewModel.observeUserMedicineListLiveData().observe(this, userMedicinesMap -> {
            if (userMedicinesMap == null || userMedicinesMap.isEmpty()) {
                medicineEmptyView.setVisibility(View.VISIBLE);
                userMedicinesRecyclerView.setVisibility(View.GONE);
            } else {
                medicineEmptyView.setVisibility(View.GONE);
                userMedicinesRecyclerView.setVisibility(View.VISIBLE);

                UserMedicinesAdapter userMedicinesAdapter = new UserMedicinesAdapter(userMedicinesMap);
                userMedicinesRecyclerView.setAdapter(userMedicinesAdapter);
            }
        });
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}