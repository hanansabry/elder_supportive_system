package com.android.patienttrackingsystem.di;

import com.android.patienttrackingsystem.SplashActivity;
import com.android.patienttrackingsystem.di.modules.AuthenticationViewModelModule;
import com.android.patienttrackingsystem.di.modules.DiseasesViewModelModule;
import com.android.patienttrackingsystem.di.modules.MedicalProfileViewModelModule;
import com.android.patienttrackingsystem.di.modules.MedicinesViewModelModule;
import com.android.patienttrackingsystem.presentation.CheckoutMedicalProfileActivity;
import com.android.patienttrackingsystem.presentation.SignInActivity;
import com.android.patienttrackingsystem.presentation.admin.AdminActivity;
import com.android.patienttrackingsystem.presentation.admin.diseases.DiseaseListActivity;
import com.android.patienttrackingsystem.presentation.admin.medicines.MedicineListActivity;
import com.android.patienttrackingsystem.presentation.SignUpActivity;
import com.android.patienttrackingsystem.presentation.user.MedicalProfileActivity;
import com.android.patienttrackingsystem.presentation.viewmodels.MedicalProfileViewModel;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector
    abstract AdminActivity contributeAdminActivity();

    @ContributesAndroidInjector (modules = AuthenticationViewModelModule.class)
    abstract SignInActivity contributeSignInActivity();

    @ContributesAndroidInjector (modules = AuthenticationViewModelModule.class)
    abstract SignUpActivity contributeSignUpActivity();

    @ContributesAndroidInjector (modules = MedicinesViewModelModule.class)
    abstract MedicineListActivity contributeMedicineListActivity();

    @ContributesAndroidInjector (modules = DiseasesViewModelModule.class)
    abstract DiseaseListActivity contributeDiseaseListActivity();

    @ContributesAndroidInjector (modules = MedicalProfileViewModelModule.class)
    abstract MedicalProfileActivity contributeMedicalProfileViewModel();

    @ContributesAndroidInjector (modules = MedicalProfileViewModelModule.class)
    abstract CheckoutMedicalProfileActivity contributeCheckoutMedicalProfileActivity();
}
