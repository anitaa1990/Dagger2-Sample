package com.an.dagger.di.module

import com.an.dagger.ui.fragment.MovieListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    /*
     * We define the name of the Fragment we are going
     * to inject the ViewModelFactory into. i.e. in our case
     * The name of the fragment: MovieListFragment
     */
    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment
}