package com.android.elderlysupportivesystem.di.modules;

import com.android.elderlysupportivesystem.di.ViewModelKey;
import com.android.elderlysupportivesystem.presentation.viewmodels.DiseasesViewModel;

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
