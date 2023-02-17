package com.android.patienttrackingsystem.presentation.admin.medicines;

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
 * Use the {@link MedicineConflictsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineConflictsFragment extends BottomSheetDialogFragment {

    public static final String TAG = "MedicineConflictsFragment.class";

    @BindView(R.id.conflicts_list_layout)
    View conflictListView;
    @BindView(R.id.add_conflict_layout)
    View addConflictLayout;
    @BindView(R.id.title)
    TextView title;

    public MedicineConflictsFragment() {
        // Required empty public constructor
    }

    public static MedicineConflictsFragment newInstance() {
        return new MedicineConflictsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicine_conflicts, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.add_medicine_conflict)
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