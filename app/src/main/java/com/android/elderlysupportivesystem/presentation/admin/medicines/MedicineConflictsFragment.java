package com.android.elderlysupportivesystem.presentation.admin.medicines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.elderlysupportivesystem.R;
import com.android.elderlysupportivesystem.data.models.Conflict;
import com.android.elderlysupportivesystem.data.models.Medicine;
import com.android.elderlysupportivesystem.di.Constants;
import com.android.elderlysupportivesystem.presentation.admin.ConflictsAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
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
    @BindView(R.id.medicine_conflicts_recycler_view)
    RecyclerView conflictsRecyclerView;
    @BindView(R.id.medicine_conflict_name_edit_text)
    EditText conflictNameEditText;
    @BindView(R.id.medicine_conflict_desc_edit_text)
    EditText conflictDescEditText;

    private MedicinesCallback medicinesCallback;
    private Medicine medicine;
    private ConflictsAdapter conflictsAdapter;
    private List<Conflict> conflictList = new ArrayList<>();

    public MedicineConflictsFragment() {
        // Required empty public constructor
    }

    public static MedicineConflictsFragment newInstance(Medicine medicine) {
        MedicineConflictsFragment fragment = new MedicineConflictsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.MEDICINE, medicine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof MedicinesCallback) {
            medicinesCallback = (MedicinesCallback) getContext();
        }
        if (getArguments() != null) {
            medicine = getArguments().getParcelable(Constants.MEDICINE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicine_conflicts, container, false);
        ButterKnife.bind(this, view);

        //set conflicts recycler view
        conflictsAdapter = new ConflictsAdapter(conflictList);
        conflictsRecyclerView.setAdapter(conflictsAdapter);
        if (medicine.getConflicts() == null || medicine.getConflicts().isEmpty()) {
            Toast.makeText(getContext(), "No current conflicts", Toast.LENGTH_SHORT).show();
        } else {
            conflictList.addAll(convertConflictsMapToList(medicine.getConflicts()));
            conflictsAdapter.notifyDataSetChanged();
        }
        return view;
    }

    private List<Conflict> convertConflictsMapToList(HashMap<String, Conflict> conflicts) {
        List<Conflict> conflictList = new ArrayList<>();
        for (String key : conflicts.keySet()) {
            conflictList.add(conflicts.get(key));
        }
        return conflictList;
    }

    @OnClick(R.id.add_medicine_conflict)
    public void onAddConflictClicked() {
        title.setText(R.string.add_new_conflict);
        addConflictLayout.setVisibility(View.VISIBLE);
        conflictListView.setVisibility(View.GONE);
        conflictNameEditText.setText("");
        conflictDescEditText.setText("");
    }

    @OnClick(R.id.save_medicine_conflict)
    public void onSaveConflict() {
        String conflictName = conflictNameEditText.getText().toString();
        String conflictDesc = conflictDescEditText.getText().toString();

        if (conflictName.isEmpty() || conflictDesc.isEmpty()) {
            Toast.makeText(getContext(), "You must enter name and description", Toast.LENGTH_SHORT).show();
        } else {
            Conflict conflict = new Conflict();
            conflict.setName(conflictName);
            conflict.setDesc(conflictDesc);
            conflictsAdapter.addConflict(conflict);
            medicinesCallback.onAddMedicineConflict(conflict, medicine.getId());
            dismiss();
        }
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