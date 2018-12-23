package com.an.dagger.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.an.dagger.R;
import com.an.dagger.databinding.MainActivityBinding;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    /*
     * Step 1: Rather than injecting the ViewModelFactory
     * in the activity, we are going to implement the
     * HasActivityInjector and inject the ViewModelFactory
     * into our MovieListFragment
     * */
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    /*
     * I am using DataBinding
     * */
    private MainActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*
         * Step 2: We still need to inject this method
         * into our activity so that our fragment can
         * inject the ViewModelFactory
         * */
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        initialiseView();
    }

    /*
     * Initialising the View using Data Binding
     * */
    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    public void updateBackground(String url) {
        binding.overlayLayout.updateCurrentBackground(url);
    }
}
