package com.android.elderlysupportivesystem.presentation.viewmodels;

import android.graphics.Bitmap;

import com.android.elderlysupportivesystem.data.DatabaseRepository;
import com.android.elderlysupportivesystem.data.models.User;
import com.google.firebase.auth.FirebaseAuth;

import net.glxn.qrgen.android.QRCode;

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
    private final MediatorLiveData<Boolean> qrCodeUrlState = new MediatorLiveData<>();
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

    public Bitmap generateQrCode(String id) {
        return QRCode.from(id).withSize(1080, 1080).bitmap();
    }

    public void saveQrCodeToFirebaseStorage(String userId, Bitmap qrCode) {
        SingleObserver<Boolean> singleObserver = databaseRepository.saveQrCode(userId, qrCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Boolean success) {
                        qrCodeUrlState.setValue(success);
                    }

                    @Override
                    public void onError(Throwable e) {
                        errorState.setValue(e.getMessage());
                    }
                });
    }

    public MediatorLiveData<Boolean> observeSavingQrCodeUrlState() {
        return qrCodeUrlState;
    }
}
