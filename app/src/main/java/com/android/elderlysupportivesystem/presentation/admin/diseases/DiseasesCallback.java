package com.android.elderlysupportivesystem.presentation.admin.diseases;

import com.android.elderlysupportivesystem.data.models.Conflict;
import com.android.elderlysupportivesystem.data.models.Disease;

public interface DiseasesCallback {
    void onDiseaseClicked(Disease disease);

    void onAddNewDisease(Disease disease);

    void onAddDiseaseConflict(Conflict conflict, String diseaseId);
}