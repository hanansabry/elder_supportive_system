package com.android.elderlysupportivesystem.di.modules;

import com.android.elderlysupportivesystem.di.ViewModelKey;
import com.android.elderlysupportivesystem.presentation.viewmodels.AuthenticationViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthenticationViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel.class)
    public abstract ViewModel bindViewModel(AuthenticationViewModel viewModel);
}
