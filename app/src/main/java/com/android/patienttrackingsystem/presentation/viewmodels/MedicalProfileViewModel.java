package com.android.patienttrackingsystem.presentation.viewmodels;

import com.android.patienttrackingsystem.data.DatabaseRepository;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.data.models.Medicine;
import com.android.patienttrackingsystem.di.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MedicalProfileViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<Boolean> addDiseaseState = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addMedicineState = new MediatorLiveData<>();
    private final MediatorLiveData<List<Disease>> allDiseaseListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<List<Medicine>> allMedicinesListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<Disease, String>> userDiseaseListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<Medicine, String>> userMedicineListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<String> qrCodeState = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();


    @Inject
    public MedicalProfileViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveAllDiseases() {
        databaseRepository.retrieveDiseases()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<Disease>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Disease> diseaseList) {
                        allDiseaseListLiveData.setValue(diseaseList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        allDiseaseListLiveData.setValue(null);
                    }
                });
    }

    public void addUserDisease(Disease disease, String status, String userId) {
        String diseaseKey = FirebaseDatabase.getInstance().getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.DISEASES_NODE)
                .push().getKey();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("disease", disease);
        updates.put("status", status);
        FirebaseDatabase.getInstance().getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.DISEASES_NODE)
                .child(diseaseKey)
                .updateChildren(updates);
    }

    public void retrieveUserDiseases(String userId) {
        FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.DISEASES_NODE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<Disease, String> userDiseasesMap = new HashMap<>();
                        for (DataSnapshot diseaseSnapshot : snapshot.getChildren()) {
                            Disease disease = diseaseSnapshot.child("disease").getValue(Disease.class);
                            String status = diseaseSnapshot.child("status").getValue(String.class);
                            userDiseasesMap.put(disease, status);
                        }
                        userDiseaseListLiveData.setValue(userDiseasesMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorState.setValue(error.getMessage());
                    }
                });
    }

    public void retrieveAllMedicines() {
        databaseRepository.retrieveMedicines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<Medicine>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Medicine> medicineList) {
                        allMedicinesListLiveData.setValue(medicineList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        allDiseaseListLiveData.setValue(null);
                    }
                });
    }

    public void addUserMedicine(Medicine medicine, String notes, String userId) {
        String medicineKey = FirebaseDatabase.getInstance().getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.MEDICINES_NODE)
                .push().getKey();
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("medicine", medicine);
        updates.put("notes", notes);
        FirebaseDatabase.getInstance().getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.MEDICINES_NODE)
                .child(medicineKey)
                .updateChildren(updates);
    }

    public void retrieveUserMedicines(String userId) {
        FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.MEDICINES_NODE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<Medicine, String> userMedicinesMap = new HashMap<>();
                        for (DataSnapshot medicineSnapshot : snapshot.getChildren()) {
                            Medicine medicine = medicineSnapshot.child("medicine").getValue(Medicine.class);
                            String status = medicineSnapshot.child("notes").getValue(String.class);
                            userMedicinesMap.put(medicine, status);
                        }
                        userMedicineListLiveData.setValue(userMedicinesMap);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorState.setValue(error.getMessage());
                    }
                });
    }

    public void retrieveUserQrCode(String userId) {
        FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.QR_CODE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String qrCodeUrl = snapshot.getValue(String.class);
                        qrCodeState.setValue(qrCodeUrl);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorState.setValue(error.getMessage());
                    }
                });
    }

    public MediatorLiveData<Boolean> observeAddDiseaseState() {
        return addDiseaseState;
    }

    public MediatorLiveData<Boolean> observeAddMedicineState() {
        return addMedicineState;
    }

    public MediatorLiveData<List<Disease>> observeAllDiseaseListLiveData() {
        return allDiseaseListLiveData;
    }

    public MediatorLiveData<List<Medicine>> observeAllMedicinesListLiveData() {
        return allMedicinesListLiveData;
    }

    public MediatorLiveData<HashMap<Disease, String>> observeUserDiseaseListLiveData() {
        return userDiseaseListLiveData;
    }

    public MediatorLiveData<HashMap<Medicine, String>> observeUserMedicineListLiveData() {
        return userMedicineListLiveData;
    }

    public MediatorLiveData<String> observeQrCodeState() {
        return qrCodeState;
    }

    public MediatorLiveData<String> getErrorState() {
        return errorState;
    }
}
