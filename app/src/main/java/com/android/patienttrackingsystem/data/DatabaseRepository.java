package com.android.patienttrackingsystem.data;

import android.graphics.Bitmap;

import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.data.models.Medicine;
import com.android.patienttrackingsystem.data.models.User;
import com.android.patienttrackingsystem.datasource.FirebaseDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DatabaseRepository {

    private final FirebaseDataSource firebaseDataSource;

    @Inject
    public DatabaseRepository(FirebaseDataSource firebaseDataSource) {
        this.firebaseDataSource = firebaseDataSource;
    }

    public Single<User> signIn(String email, String password, boolean loginAsAdmin) {
        return firebaseDataSource.signIn(email, password, loginAsAdmin);
    }

    public Single<User> signUp(User user) {
        return firebaseDataSource.signUp(user);
    }

    public Single<Boolean> addNewMedicine(Medicine medicine) {
        return firebaseDataSource.addNewMedicine(medicine);
    }

    public Flowable<List<Medicine>> retrieveMedicines() {
        return firebaseDataSource.retrieveMedicines();
    }

    public Single<Boolean> addMedicineConflict(Conflict conflict, String medicineId) {
        return firebaseDataSource.addMedicineConflict(conflict, medicineId);
    }

    public Single<Boolean> addNewDisease(Disease disease) {
        return firebaseDataSource.addNewDisease(disease);
    }

    public Flowable<List<Disease>> retrieveDiseases() {
        return firebaseDataSource.retrieveDiseases();
    }

    public Single<Boolean> addDiseaseConflict(Conflict conflict, String diseaseId) {
        return firebaseDataSource.addDiseaseConflict(conflict, diseaseId);
    }

    public Single<Boolean> saveQrCode(String userId, Bitmap qrCode) {
        return firebaseDataSource.saveQrCode(userId, qrCode);
    }
}
