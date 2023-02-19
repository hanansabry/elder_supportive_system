package com.android.patienttrackingsystem.presentation.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Disease;

import java.util.HashMap;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDiseasesAdapter extends RecyclerView.Adapter<UserDiseasesAdapter.UserDiseaseViewHolder> {

    private HashMap<Disease, String> userDiseasesMap;
    private Disease[] userDiseasesKeySet;

    public UserDiseasesAdapter(HashMap<Disease, String> userDiseasesMap) {
        this.userDiseasesMap = userDiseasesMap;
        // get the key set
        Set<Disease> keySet = userDiseasesMap.keySet();
        userDiseasesKeySet = userDiseasesMap.keySet().toArray(new Disease[keySet.size()]);
    }

    @NonNull
    @Override
    public UserDiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_disease_item_layout, parent, false);
        return new UserDiseaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDiseaseViewHolder holder, int position) {
        Disease selectedDiseaseKey = userDiseasesKeySet[position];
        String status = userDiseasesMap.get(selectedDiseaseKey);

        holder.diseaseName.setText(selectedDiseaseKey.getName());
        holder.status.setText(status);
        if (selectedDiseaseKey.getConflicts() != null && !selectedDiseaseKey.getConflicts().isEmpty()) {
            holder.conflictsLayout.setVisibility(View.VISIBLE);
            StringBuilder conflicts = new StringBuilder();
            for (String conflictId : selectedDiseaseKey.getConflicts().keySet()) {
                Conflict conflict = selectedDiseaseKey.getConflicts().get(conflictId);
                conflicts.append(conflict.getName()).append(" - ");
            }
            holder.conflictsText.setText(conflicts);
        } else {
            holder.conflictsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userDiseasesMap.size();
    }

    static class UserDiseaseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.disease_name)
        TextView diseaseName;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.conflicts_layout)
        View conflictsLayout;
        @BindView(R.id.conflicts)
        TextView conflictsText;

        public UserDiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
