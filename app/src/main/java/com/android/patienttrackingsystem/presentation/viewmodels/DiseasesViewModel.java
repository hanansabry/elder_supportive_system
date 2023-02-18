package com.android.patienttrackingsystem.presentation.viewmodels;

import com.android.patienttrackingsystem.data.DatabaseRepository;
import com.android.patienttrackingsystem.data.models.Conflict;
import com.android.patienttrackingsystem.data.models.Disease;
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

public class DiseasesViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<List<Disease>> diseaseListLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addDiseaseState = new MediatorLiveData<>();
    private final MediatorLiveData<Boolean> addConflictState = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public DiseasesViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void retrieveDiseases() {
        databaseRepository.retrieveDiseases()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(new Observer<List<Disease>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(List<Disease> diseaseList) {
                        diseaseListLiveData.setValue(diseaseList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        diseaseListLiveData.setValue(null);
                    }
                });
    }

    public void addNewDisease(Disease disease) {
        SingleObserver<Boolean> singleObserver = databaseRepository.addNewDisease(disease)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        addDiseaseState.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void addDiseaseConflict(Conflict conflict, String diseaseId) {
        SingleObserver<Boolean> singleObserver = databaseRepository.addDiseaseConflict(conflict, diseaseId)
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

    public MediatorLiveData<List<Disease>> observeDiseaseListLiveData() {
        return diseaseListLiveData;
    }

    public MediatorLiveData<Boolean> observeAddDiseaseState() {
        return addDiseaseState;
    }

    public MediatorLiveData<Boolean> observeAddConflictState() {
        return addConflictState;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }
}
