package com.an.dagger.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.an.dagger.di.ViewModelKey;
import com.an.dagger.factory.ViewModelFactory;
import com.an.dagger.ui.viewmodel.MovieListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);


    /*
     * This method basically says
     * inject this object into a Map using the @IntoMap annotation,
     * with the  MovieListViewModel.class as key,
     * and a Provider that will build a MovieListViewModel
     * object.
     *
     * */

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel.class)
    protected abstract ViewModel movieListViewModel(MovieListViewModel moviesListViewModel);
}