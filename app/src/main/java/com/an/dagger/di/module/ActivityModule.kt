package com.an.dagger.di.module

import com.an.dagger.ui.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    /*
     * We modify our ActivityModule by adding the
     * FragmentModule to the Activity which contains
     * the fragment
     */
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}