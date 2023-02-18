package com.android.patienttrackingsystem.di.modules;

import com.android.patienttrackingsystem.di.ViewModelKey;
import com.android.patienttrackingsystem.presentation.viewmodels.DiseasesViewModel;
import com.android.patienttrackingsystem.presentation.viewmodels.MedicinesViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class DiseasesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DiseasesViewModel.class)
    public abstract ViewModel bindViewModel(DiseasesViewModel viewModel);
}
