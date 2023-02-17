package com.android.patienttrackingsystem.presentation.admin.diseases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.patienttrackingsystem.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiseaseConflictsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiseaseConflictsFragment extends BottomSheetDialogFragment {

    public static final String TAG = "DiseaseConflictsFragment.class";

    @BindView(R.id.conflicts_list_layout)
    View conflictListView;
    @BindView(R.id.add_conflict_layout)
    View addConflictLayout;
    @BindView(R.id.title)
    TextView title;

    public DiseaseConflictsFragment() {
        // Required empty public constructor
    }

    public static DiseaseConflictsFragment newInstance() {
        return new DiseaseConflictsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disease_conflicts, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.add_disease_conflict)
    public void onAddConflictClicked() {
        title.setText(R.string.add_new_conflict);
        addConflictLayout.setVisibility(View.VISIBLE);
        conflictListView.setVisibility(View.GONE);
    }

    @OnClick(R.id.back_button)
    public void onBackClicked() {
        dismiss();
    }

    @Override
    public void dismiss() {
        if (addConflictLayout.getVisibility() == View.VISIBLE) {
            title.setText(R.string.medicine_conflicts);
            addConflictLayout.setVisibility(View.GONE);
            conflictListView.setVisibility(View.VISIBLE);
        } else {
            super.dismiss();
        }
    }
}