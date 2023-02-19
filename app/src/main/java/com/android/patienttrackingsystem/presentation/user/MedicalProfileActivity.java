package com.android.patienttrackingsystem.presentation.user;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.data.models.Medicine;
import com.android.patienttrackingsystem.datasource.SharedPreferencesDataSource;
import com.android.patienttrackingsystem.di.Constants;
import com.android.patienttrackingsystem.di.ViewModelProviderFactory;
import com.android.patienttrackingsystem.presentation.SignInActivity;
import com.android.patienttrackingsystem.presentation.viewmodels.MedicalProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class MedicalProfileActivity extends DaggerAppCompatActivity implements MedicalProfileCallback{

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private String userId;
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
        setContentView(R.layout.activity_medical_profile);
        ButterKnife.bind(this);

        userId = sharedPreferencesDataSource.getUserId();
        medicalProfileViewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(MedicalProfileViewModel.class);
        medicalProfileViewModel.retrieveAllDiseases();
        medicalProfileViewModel.retrieveAllMedicines();
        medicalProfileViewModel.retrieveUserDiseases(userId);
        medicalProfileViewModel.retrieveUserMedicines(userId);
        observeRetrieveAllDiseases();
        observeRetrieveAllMedicines();
        observeRetrieveUserDiseases();
        observeRetrieveUserMedicines();
        observeUserQrCode();
        //detectShakeEvent
        detectShakeEvent();
    }

    private void observeUserQrCode() {
        medicalProfileViewModel.observeQrCodeState().observe(this, qrCodeUrl -> {
            Intent intent = new Intent(this, QrCodeActivity.class);
            intent.putExtra(Constants.QR_CODE, qrCodeUrl);
            startActivity(intent);
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

    private void observeRetrieveAllDiseases() {
        medicalProfileViewModel.observeAllDiseaseListLiveData().observe(this, allDiseasesList -> {
            this.allDiseasesList = (ArrayList<Disease>) allDiseasesList;
        });
    }

    private void observeRetrieveAllMedicines() {
        medicalProfileViewModel.observeAllMedicinesListLiveData().observe(this, allMedicineList -> {
            this.allMedicineList = (ArrayList<Medicine>) allMedicineList;
        });
    }

    @OnClick(R.id.add_disease)
    public void onAddNewDiseaseClicked() {
        List<String> statues = Arrays.asList(getResources().getStringArray(R.array.statues));
        AddUserDiseaseFragment addUserDiseaseFragment = AddUserDiseaseFragment.newInstance(allDiseasesList, new ArrayList<>(statues));
        addUserDiseaseFragment.show(getSupportFragmentManager(), AddUserDiseaseFragment.TAG);
    }

    @OnClick(R.id.add_medicine)
    public void onAddNewMedicineClicked() {
        AddUserMedicineFragment addUserMedicineFragment = AddUserMedicineFragment.newInstance(allMedicineList);
        addUserMedicineFragment.show(getSupportFragmentManager(), AddUserMedicineFragment.TAG);
    }

    @OnClick(R.id.send_code)
    public void onSendQrCodeClicked() {
        medicalProfileViewModel.retrieveUserQrCode(userId);
    }

    private void detectShakeEvent() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 10) {
                medicalProfileViewModel.retrieveUserQrCode(userId);
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    public void onAddNewUserDisease(Disease selectedDisease, String status) {
        medicalProfileViewModel.addUserDisease(selectedDisease, status, userId);
        Toast.makeText(this, "New user is added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddNewUserMedicine(Medicine selectedMedicine, String notes) {
        medicalProfileViewModel.addUserMedicine(selectedMedicine, notes, userId);
    }

    @OnClick(R.id.sign_out)
    public void onSignOutClicked() {
        FirebaseAuth.getInstance().signOut();
        sharedPreferencesDataSource.removeAllValues();
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        onBackPressed();
    }
}