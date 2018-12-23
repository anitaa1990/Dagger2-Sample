package com.an.dagger.di.component;

import android.app.Application;

import com.an.dagger.AppController;
import com.an.dagger.di.module.ActivityModule;
import com.an.dagger.di.module.ApiModule;
import com.an.dagger.di.module.DbModule;
import com.an.dagger.di.module.FragmentModule;
import com.an.dagger.di.module.ViewModelModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


/*
 * We mark this interface with the @Component annotation.
 * And we define all the modules that can be injected.
 * Note that we provide AndroidSupportInjectionModule.class
 * here. This class was not created by us.
 * It is an internal class in Dagger 2.10.
 * Provides our activities and fragments with given module.
 * */
@Component(modules = {
                ApiModule.class,
                DbModule.class,
                ViewModelModule.class,
                ActivityModule.class,
                FragmentModule.class,
                AndroidSupportInjectionModule.class})
@Singleton
public interface AppComponent {


    /* We will call this builder interface from our custom Application class.
     * This will set our application object to the AppComponent.
     * So inside the AppComponent the application instance is available.
     * So this application instance can be accessed by our modules
     * such as ApiModule when needed
     * */
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }


    /*
     * This is our custom Application class
     * */
    void inject(AppController appController);
}
