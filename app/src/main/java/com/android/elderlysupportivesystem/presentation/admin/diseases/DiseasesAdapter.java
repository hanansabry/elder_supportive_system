package com.android.elderlysupportivesystem.presentation.admin.diseases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Disease;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DiseasesAdapter extends RecyclerView.Adapter<DiseasesAdapter.DiseaseViewHolder> {

    private List<Disease> diseaseList;
    private DiseasesCallback diseasesCallback;

    public DiseasesAdapter(List<Disease> diseaseList, DiseasesCallback diseasesCallback) {
        this.diseaseList = diseaseList;
        this.diseasesCallback = diseasesCallback;
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disease_item_layout, parent, false);
        return new DiseaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        Disease disease = diseaseList.get(position);
        holder.diseaseName.setText(disease.getName());
        holder.diseaseDesc.setText(disease.getDesc());
        holder.conflictsButton.setOnClickListener(v -> diseasesCallback.onDiseaseClicked(disease));
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    class DiseaseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.disease_name)
        TextView diseaseName;
        @BindView(R.id.disease_desc)
        TextView diseaseDesc;
        @BindView(R.id.conflicts_button)
        Button conflictsButton;

        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
