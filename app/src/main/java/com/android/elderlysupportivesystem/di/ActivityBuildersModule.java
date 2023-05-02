package com.android.elderlysupportivesystem.di;

import com.android.elderlysupportivesystem.SplashActivity;
import com.android.elderlysupportivesystem.di.modules.AuthenticationViewModelModule;
import com.android.elderlysupportivesystem.di.modules.DiseasesViewModelModule;
import com.android.elderlysupportivesystem.di.modules.MedicalProfileViewModelModule;
import com.android.elderlysupportivesystem.di.modules.MedicinesViewModelModule;
import com.android.elderlysupportivesystem.presentation.CheckoutMedicalProfileActivity;
import com.android.elderlysupportivesystem.presentation.SignInActivity;
import com.android.elderlysupportivesystem.presentation.admin.AdminActivity;
import com.android.elderlysupportivesystem.presentation.admin.diseases.DiseaseListActivity;
import com.android.elderlysupportivesystem.presentation.admin.medicines.MedicineListActivity;
import com.android.elderlysupportivesystem.presentation.SignUpActivity;
import com.android.elderlysupportivesystem.presentation.user.MedicalProfileActivity;

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
