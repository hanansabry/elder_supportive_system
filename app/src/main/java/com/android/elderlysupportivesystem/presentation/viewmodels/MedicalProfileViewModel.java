package com.android.elderlysupportivesystem.presentation.viewmodels;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.android.elderlysupportivesystem.data.DatabaseRepository;
import com.android.elderlysupportivesystem.data.models.Disease;
import com.android.elderlysupportivesystem.data.models.Medicine;
import com.android.elderlysupportivesystem.data.models.Relative;
import com.android.elderlysupportivesystem.data.models.User;
import com.android.elderlysupportivesystem.di.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    private final MediatorLiveData<User> userState = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addDiseaseState = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addMedicineState = new MediatorLiveData<>();
    private final MediatorLiveData<List<Disease>> allDiseaseListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<List<Medicine>> allMedicinesListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<Disease, String>> userDiseaseListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<Medicine, String>> userMedicineListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<List<Relative>> userRelativeListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<String> qrCodeState = new MediatorLiveData<>();
    private final MediatorLiveData<String> userLocationAddressState = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();


    @Inject
    public MedicalProfileViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveUserData(String userId) {
        FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_NODE)
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        userState.setValue(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorState.setValue(error.getMessage());
                    }
                });
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

    public void addUserRelative(Relative relative, String userId) {
        FirebaseDatabase.getInstance().getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.RELATIVES_NODE)
                .push()
                .setValue(relative);
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

    public void retrieveUserRelatives(String userId) {
        FirebaseDatabase.getInstance()
                .getReference(Constants.USERS_NODE)
                .child(userId)
                .child(Constants.RELATIVES_NODE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Relative> relativeList = new ArrayList<>();
                        for (DataSnapshot relativeSnapshot : snapshot.getChildren()) {
                            Relative relative = relativeSnapshot.getValue(Relative.class);
                            relative.setId(relativeSnapshot.getKey());
                            relativeList.add(relative);
                        }
                        userRelativeListLiveData.setValue(relativeList);
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

    public void getUserAddress(Context context, double latitude, double longitude) {
        Thread thread = new Thread(() -> {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String area = addresses.get(0).getSubAdminArea();
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                userLocationAddressState.postValue(address);
            } catch (IOException e) {
                e.printStackTrace();
                errorState.setValue(e.getMessage());
            }
        });
        thread.start();
    }

    public MediatorLiveData<User> observeUserState() {
        return userState;
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

    public MediatorLiveData<List<Relative>> observeUserRelativesListLiveData() {
        return userRelativeListLiveData;
    }

    public MediatorLiveData<String> observeQrCodeState() {
        return qrCodeState;
    }

    public MediatorLiveData<String> observeUserLocationAddressState() {
        return userLocationAddressState;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }
}
