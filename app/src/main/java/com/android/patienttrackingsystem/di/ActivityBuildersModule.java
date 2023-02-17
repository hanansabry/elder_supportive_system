package com.android.patienttrackingsystem.di;

import com.android.patienttrackingsystem.SplashActivity;
import com.android.patienttrackingsystem.di.modules.AuthenticationViewModelModule;
import com.android.patienttrackingsystem.di.modules.MedicinesViewModelModule;
import com.android.patienttrackingsystem.presentation.SignInActivity;
import com.android.patienttrackingsystem.presentation.admin.medicines.MedicineListActivity;
import com.android.patienttrackingsystem.presentation.viewmodels.AuthenticationViewModel;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector (modules = AuthenticationViewModelModule.class)
    abstract SignInActivity contributeSignInActivity();

    @ContributesAndroidInjector (modules = MedicinesViewModelModule.class)
    abstract MedicineListActivity contributeMedicineListActivity();

}
