package com.an.dagger.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.an.dagger.data.local.AppDatabase;
import com.an.dagger.data.local.dao.MovieDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    /*
     * The method returns the Database object
     * */
    @Provides
    @Singleton
    AppDatabase provideDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "Entertainment.db")
                .allowMainThreadQueries().build();
    }


    /*
     * We need the MovieDao module.
     * For this, We need the AppDatabase object
     * So we will define the providers for this here in this module.
     * */

    @Provides
    @Singleton
    MovieDao provideMovieDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.movieDao();
    }
}
