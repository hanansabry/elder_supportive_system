package com.android.patienttrackingsystem.datasource;

import android.graphics.Bitmap;
import android.net.Uri;

import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.data.models.Medicine;
import com.android.patienttrackingsystem.data.models.User;
import com.android.patienttrackingsystem.di.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class FirebaseDataSource {

    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private SharedPreferencesDataSource sharedPreferencesDataSource;

    @Inject
    public FirebaseDataSource(StorageReference storageReference
            , FirebaseDatabase firebaseDatabase
            , FirebaseAuth firebaseAuth
            , SharedPreferencesDataSource sharedPreferencesDataSource) {
        this.storageReference = storageReference;
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        this.sharedPreferencesDataSource = sharedPreferencesDataSource;
    }

    public Single<User> signIn(String email, String password, boolean loginAsAdmin) {
        return Single.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = task.getResult().getUser().getUid();
                            DatabaseReference userRef = firebaseDatabase.getReference(Constants.USERS_NODE).child(userId);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);
                                    if (user != null) {
                                        user.setId(userId);
                                        if (loginAsAdmin && user.getRole() != null) {
                                            emitter.onSuccess(user);
                                        } else if (!loginAsAdmin && user.getRole() == null) {
                                            emitter.onSuccess(user);
                                        } else {
                                            emitter.onError(new Throwable("error"));
                                        }
                                    } else {
                                        emitter.onError(new Throwable("error"));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    emitter.onError(new Throwable("error"));
                                }
                            });
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    public Single<User> signUp(User user) {
        return Single.create(emitter -> firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = task.getResult().getUser().getUid();
                        DatabaseReference usersRef = firebaseDatabase.getReference(Constants.USERS_NODE);
                        user.setId(userId);
                        usersRef.child(userId).setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                emitter.onSuccess(user);
                            } else {
                                emitter.onError(task1.getException());
                            }
                        });
                    } else {
                        emitter.onError(task.getException());
                    }
                }));
    }

    public Single<Boolean> addNewMedicine(Medicine medicine) {
        return Single.create(emitter -> {
            DatabaseReference medicinesRef = firebaseDatabase.getReference(Constants.MEDICINES_NODE);
            medicinesRef.push().setValue(medicine)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Single<Boolean> addMedicineConflict(Conflict conflict, String medicineId) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constants.MEDICINES_NODE)
                    .child(medicineId)
                    .child(Constants.CONFLICTS)
                    .push()
                    .setValue(conflict)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Flowable<List<Medicine>> retrieveMedicines() {
        return Flowable.create(emitter -> {
            DatabaseReference medicinesRef = firebaseDatabase.getReference(Constants.MEDICINES_NODE);
            medicinesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Medicine> medicineList = new ArrayList<>();
                    for (DataSnapshot medicineSnapshot : snapshot.getChildren()) {
                        Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                        medicine.setId(medicineSnapshot.getKey());
                        medicineList.add(medicine);
                    }
                    emitter.onNext(medicineList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Single<Boolean> addNewDisease(Disease disease) {
        return Single.create(emitter -> {
            DatabaseReference diseasesRef = firebaseDatabase.getReference(Constants.DISEASES_NODE);
            diseasesRef.push().setValue(disease)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Single<Boolean> addDiseaseConflict(Conflict conflict, String diseaseId) {
        return Single.create(emitter -> {
            firebaseDatabase.getReference(Constants.DISEASES_NODE)
                    .child(diseaseId)
                    .child(Constants.CONFLICTS)
                    .push()
                    .setValue(conflict)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onSuccess(false);
                        }
                    });
        });
    }

    public Flowable<List<Disease>> retrieveDiseases() {
        return Flowable.create(emitter -> {
            DatabaseReference medicinesRef = firebaseDatabase.getReference(Constants.DISEASES_NODE);
            medicinesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Disease> diseaseList = new ArrayList<>();
                    for (DataSnapshot diseaseSnapshot : snapshot.getChildren()) {
                        Disease disease = diseaseSnapshot.getValue(Disease.class);
                        disease.setId(diseaseSnapshot.getKey());
                        diseaseList.add(disease);
                    }
                    emitter.onNext(diseaseList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        }, BackpressureStrategy.BUFFER);
    }

    public Single<Boolean> saveQrCode(String userId, Bitmap qrCode) {
        return Single.create(emitter -> {
            // Create a reference to "qr_code.jpg"
            StorageReference reference = storageReference.child(Constants.QR_CODE_FOLDER + "/" + userId + "/qr_code_" + System.currentTimeMillis() + Constants.IMAGE_FILE_TYPE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            qrCode.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = reference.putBytes(data);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    emitter.onError(task.getException());
                } else {
                    // Continue with the fileTask to get the download URL
                    reference.getDownloadUrl().addOnCompleteListener(task1 -> {
                        String downloadUrl = task1.getResult().toString();
                        firebaseDatabase.getReference(Constants.USERS_NODE)
                                .child(userId)
                                .child("qr_code")
                                .setValue(downloadUrl)
                                .addOnCompleteListener(saveTask -> {
                                    if (saveTask.isSuccessful()) {
                                        emitter.onSuccess(true);
                                    } else {
                                        emitter.onSuccess(false);
                                    }
                                });
                    });
                }
                return reference.getDownloadUrl();
            });

        });
    }
}
