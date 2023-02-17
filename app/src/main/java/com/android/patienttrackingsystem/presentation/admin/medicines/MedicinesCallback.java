package com.android.patienttrackingsystem.presentation.admin.medicines;

import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Medicine;

public interface MedicinesCallback {
    void onMedicineClicked(Medicine medicine);

    void onAddNewMedicine(Medicine medicine);

    void onAddMedicineConflict(Conflict conflict, String medicineId);
}