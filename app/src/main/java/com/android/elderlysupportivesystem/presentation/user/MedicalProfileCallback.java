package com.android.elderlysupportivesystem.presentation.user;

import com.android.elderlysupportivesystem.data.models.Disease;
import com.android.elderlysupportivesystem.data.models.Medicine;

public interface MedicalProfileCallback {
    void onAddNewUserDisease(Disease selectedDisease, String status);

    void onAddNewUserMedicine(Medicine selectedMedicine, String notes);
}
