package com.android.elderlysupportivesystem.presentation.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Medicine;

import java.util.HashMap;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserMedicinesAdapter extends RecyclerView.Adapter<UserMedicinesAdapter.UserMedicineViewHolder> {

    private HashMap<Medicine, String> userMedicinesMap;
    private Medicine[] userMedicinesKeySet;

    public UserMedicinesAdapter(HashMap<Medicine, String> userMedicinesMap) {
        this.userMedicinesMap = userMedicinesMap;
        // get the key set
        Set<Medicine> keySet = userMedicinesMap.keySet();
        userMedicinesKeySet = userMedicinesMap.keySet().toArray(new Medicine[keySet.size()]);
    }


    @NonNull
    @Override
    public UserMedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_medicine_item_layout, parent, false);
        return new UserMedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMedicineViewHolder holder, int position) {
        Medicine selectedMedicineKey = userMedicinesKeySet[position];
        String notes = userMedicinesMap.get(selectedMedicineKey);

        holder.medicineName.setText(selectedMedicineKey.getName());
        holder.medicineNotes.setText(notes);
    }

    @Override
    public int getItemCount() {
        return userMedicinesMap.size();
    }

    static class UserMedicineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.medicine_name)
        TextView medicineName;
        @BindView(R.id.medicine_notes)
        TextView medicineNotes;

        public UserMedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
