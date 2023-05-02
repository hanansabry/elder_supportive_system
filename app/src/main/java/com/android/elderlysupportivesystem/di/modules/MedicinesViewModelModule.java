package com.android.elderlysupportivesystem.di.modules;

import com.android.elderlysupportivesystem.di.ViewModelKey;
import com.android.elderlysupportivesystem.presentation.viewmodels.MedicinesViewModel;

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
