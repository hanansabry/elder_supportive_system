package com.android.patienttrackingsystem.data;

import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Medicine;
import com.android.patienttrackingsystem.data.models.User;
import com.android.patienttrackingsystem.datasource.FirebaseDataSource;

import java.util.List;

import javax.inject.Inject;

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
}
