package com.android.elderlysupportivesystem.presentation.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Relative;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserRelativesAdapter extends RecyclerView.Adapter<UserRelativesAdapter.UserRelativeViewHolder> {

    private List<Relative> userRelativesList;

    public UserRelativesAdapter(List<Relative> userRelativesList) {
        this.userRelativesList = userRelativesList;
    }


    @NonNull
    @Override
    public UserRelativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_relative_item_layout, parent, false);
        return new UserRelativeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRelativeViewHolder holder, int position) {
        Relative relative = userRelativesList.get(position);
        holder.relativeName.setText(relative.getName());
        holder.relativePhone.setText(relative.getPhone());
    }

    @Override
    public int getItemCount() {
        return userRelativesList.size();
    }

    static class UserRelativeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.relative_name)
        TextView relativeName;
        @BindView(R.id.relative_number)
        TextView relativePhone;

        public UserRelativeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
