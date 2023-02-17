package com.android.patienttrackingsystem.presentation.viewmodels;

import com.android.patienttrackingsystem.data.DatabaseRepository;
import com.android.patienttrackingsystem.data.models.User;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AuthenticationViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MediatorLiveData<User> userState = new MediatorLiveData<>();
    private final MediatorLiveData<String> errorState = new MediatorLiveData<>();

    @Inject
    public AuthenticationViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    public void signIn(String email, String password, boolean loginAsAdmin) {
        SingleObserver<User> singleObserver = databaseRepository.signIn(email, password, loginAsAdmin)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(User user) {
                        userState.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void signUp(User user) {
        SingleObserver<User> singleObserver = databaseRepository.signUp(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(User currentUser) {
                        userState.setValue(currentUser);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public void signOut(User currentUser) {
        FirebaseAuth.getInstance().signOut();
    }

    public MediatorLiveData<User> observeUserState() {
        return userState;
    }

    public MediatorLiveData<String> observeErrorState() {
        return errorState;
    }

}
