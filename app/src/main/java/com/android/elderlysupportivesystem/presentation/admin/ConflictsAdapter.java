package com.android.elderlysupportivesystem.presentation.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Conflict;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConflictsAdapter extends RecyclerView.Adapter<ConflictsAdapter.ConflictViewHolder> {

    private List<Conflict> conflictList;

    public ConflictsAdapter(List<Conflict> conflictList) {
        this.conflictList = conflictList;
    }

    @NonNull
    @Override
    public ConflictViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conflict_item_layout, parent, false);
        return new ConflictViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConflictViewHolder holder, int position) {
        Conflict conflict = conflictList.get(position);
        holder.conflictName.setText(conflict.getName());
        holder.conflictDesc.setText(conflict.getDesc());
    }

    @Override
    public int getItemCount() {
        return conflictList.size();
    }

    public void addConflict(Conflict conflict) {
        conflictList.add(conflict);
        notifyItemInserted(conflictList.size());
    }

    class ConflictViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.conflict_name)
        TextView conflictName;
        @BindView(R.id.conflict_desc)
        TextView conflictDesc;

        public ConflictViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
