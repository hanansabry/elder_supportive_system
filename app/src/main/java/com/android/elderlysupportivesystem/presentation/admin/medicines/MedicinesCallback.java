package com.android.elderlysupportivesystem.presentation.admin.medicines;

import com.android.elderlysupportivesystem.data.models.Conflict;
import com.android.elderlysupportivesystem.data.models.Medicine;

public interface MedicinesCallback {
    void onMedicineClicked(Medicine medicine);

    void onAddNewMedicine(Medicine medicine);

    void onAddMedicineConflict(Conflict conflict, String medicineId);
}