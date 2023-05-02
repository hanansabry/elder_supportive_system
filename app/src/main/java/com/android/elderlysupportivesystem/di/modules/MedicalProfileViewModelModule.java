package com.android.elderlysupportivesystem.di.modules;

import com.android.elderlysupportivesystem.di.ViewModelKey;
import com.android.elderlysupportivesystem.presentation.viewmodels.MedicalProfileViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MedicalProfileViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MedicalProfileViewModel.class)
    public abstract ViewModel bindViewModel(MedicalProfileViewModel viewModel);
}
