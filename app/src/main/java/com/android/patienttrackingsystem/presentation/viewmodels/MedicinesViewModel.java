package com.android.patienttrackingsystem.presentation.viewmodels;

import com.android.patienttrackingsystem.data.DatabaseRepository;
import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Medicine;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MedicinesViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<List<Medicine>> medicineListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addMedicineState = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addConflictState = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public MedicinesViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveMedicines() {
        databaseRepository.retrieveMedicines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<Medicine>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Medicine> carList) {
                        medicineListLiveData.setValue(carList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        medicineListLiveData.setValue(null);
                    }
                });
    }

    public void addNewMedicine(Medicine medicine) {
        SingleObserver<Boolean> singleObserver = databaseRepository.addNewMedicine(medicine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addMedicineState.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void addMedicineConflict(Conflict conflict, String medicineId) {
        SingleObserver<Boolean> singleObserver = databaseRepository.addMedicineConflict(conflict, medicineId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addConflictState.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MediatorLiveData<List<Medicine>> observeMedicineListLiveData() {
        return medicineListLiveData;
    }

    public MediatorLiveData<Boolean> observeAddMedicineState() {
        return addMedicineState;
    }

    public MediatorLiveData<Boolean> observeAddConflictState() {
        return addConflictState;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }
}
