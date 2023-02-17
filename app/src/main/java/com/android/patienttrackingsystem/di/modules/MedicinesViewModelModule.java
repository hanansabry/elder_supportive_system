package com.android.patienttrackingsystem.di.modules;

import com.android.patienttrackingsystem.di.ViewModelKey;
import com.android.patienttrackingsystem.presentation.viewmodels.AuthenticationViewModel;
import com.android.patienttrackingsystem.presentation.viewmodels.MedicinesViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MedicinesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MedicinesViewModel.class)
    public abstract ViewModel bindViewModel(MedicinesViewModel viewModel);
}
