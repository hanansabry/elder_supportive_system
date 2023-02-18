package com.android.patienttrackingsystem.presentation.admin.diseases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.patienttrackingsystem.R;
import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Disease;
import com.android.patienttrackingsystem.data.models.Medicine;
import com.android.patienttrackingsystem.di.Constants;
import com.android.patienttrackingsystem.presentation.admin.ConflictsAdapter;
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
    @BindView(R.id.disease_conflicts_recycler_view)
    RecyclerView conflictsRecyclerView;
    @BindView(R.id.disease_conflict_name_edit_text)
    EditText conflictNameEditText;
    @BindView(R.id.disease_conflict_desc_edit_text)
    EditText conflictDescEditText;

    private DiseasesCallback diseasesCallback;
    private Disease disease;
    private ConflictsAdapter conflictsAdapter;
    private List<Conflict> conflictList = new ArrayList<>();

    public DiseaseConflictsFragment() {
        // Required empty public constructor
    }

    public static DiseaseConflictsFragment newInstance(Disease disease) {
        DiseaseConflictsFragment fragment = new DiseaseConflictsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.DISEASE, disease);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() instanceof DiseasesCallback) {
            diseasesCallback = (DiseasesCallback) getContext();
        }
        if (getArguments() != null) {
            disease = getArguments().getParcelable(Constants.DISEASE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disease_conflicts, container, false);
        ButterKnife.bind(this, view);

        //set conflicts recycler view
        conflictsAdapter = new ConflictsAdapter(conflictList);
        conflictsRecyclerView.setAdapter(conflictsAdapter);
        if (disease.getConflicts() == null || disease.getConflicts().isEmpty()) {
            Toast.makeText(getContext(), "No current conflicts", Toast.LENGTH_SHORT).show();
        } else {
            conflictList.addAll(convertConflictsMapToList(disease.getConflicts()));
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

    @OnClick(R.id.add_disease_conflict)
    public void onAddConflictClicked() {
        title.setText(R.string.add_new_conflict);
        addConflictLayout.setVisibility(View.VISIBLE);
        conflictListView.setVisibility(View.GONE);
        conflictNameEditText.setText("");
        conflictDescEditText.setText("");
    }

    @OnClick(R.id.save_disease_conflict)
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
            diseasesCallback.onAddDiseaseConflict(conflict, disease.getId());
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
            title.setText(R.string.disease_conflicts);
            addConflictLayout.setVisibility(View.GONE);
            conflictListView.setVisibility(View.VISIBLE);
        } else {
            super.dismiss();
        }
    }
}