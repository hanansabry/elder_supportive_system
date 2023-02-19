package com.android.patienttrackingsystem.presentation.user;

import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.data.models.Medicine;

public interface MedicalProfileCallback {
    void onAddNewUserDisease(Disease selectedDisease, String status);

    void onAddNewUserMedicine(Medicine selectedMedicine, String notes);
}
