package com.an.dagger;

import android.app.Activity;
import android.app.Application;

import com.an.dagger.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


/*
 * we use our AppComponent (now prefixed with Dagger)
 * to inject our Application class.
 * This way a DispatchingAndroidInjector is injected which is
 * then returned when an injector for an activity is requested.
 * */
public class AppController extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder()
                .application(this)
                .build()
            .inject(this);
    }
}
