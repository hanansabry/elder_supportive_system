package com.android.patienttrackingsystem.di;

import com.android.patienttrackingsystem.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

}
