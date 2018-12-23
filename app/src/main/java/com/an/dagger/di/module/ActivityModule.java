package com.an.dagger.di.module;

import com.an.dagger.ui.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();
}