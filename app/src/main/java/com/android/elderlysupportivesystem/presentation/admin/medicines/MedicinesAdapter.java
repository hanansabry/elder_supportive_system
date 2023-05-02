package com.android.elderlysupportivesystem.presentation.admin.medicines;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Medicine;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private MedicinesCallback medicinesCallback;

    public MedicinesAdapter(List<Medicine> medicineList, MedicinesCallback medicinesCallback) {
        this.medicineList = medicineList;
        this.medicinesCallback = medicinesCallback;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_item_layout, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.medicineName.setText(medicine.getName());
        holder.medicineDesc.setText(medicine.getDesc());
        holder.conflictsButton.setOnClickListener(v -> medicinesCallback.onMedicineClicked(medicine));
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    class MedicineViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.medicine_name)
        TextView medicineName;
        @BindView(R.id.medicine_desc)
        TextView medicineDesc;
        @BindView(R.id.conflicts_button)
        Button conflictsButton;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
