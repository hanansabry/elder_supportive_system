package com.android.patienttrackingsystem.presentation.admin.diseases;

import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.data.models.Medicine;

public interface DiseasesCallback {
    void onDiseaseClicked(Disease disease);

    void onAddNewDisease(Disease disease);

    void onAddDiseaseConflict(Conflict conflict, String diseaseId);
}