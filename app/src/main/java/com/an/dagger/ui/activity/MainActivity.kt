package com.an.dagger.ui.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.an.dagger.databinding.MainActivityBinding
import dagger.android.AndroidInjection
import javax.inject.Inject
import com.an.dagger.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {


    /*
     * Step 1: Rather than injecting the ViewModelFactory
     * in the activity, we are going to implement the
     * HasActivityInjector and inject the ViewModelFactory
     * into our MovieListFragment
     * */
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }


    /*
     * I am using DataBinding
     * */
    private lateinit var binding: MainActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        /*
         * Step 2: We still need to inject this method
         * into our activity so that our fragment can
         * inject the ViewModelFactory
         * */
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        initialiseView()
    }


    /*
     * Initialising the View using Data Binding
     * */
    private fun initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    fun updateBackground(url: String?) {
        binding.overlayLayout.updateCurrentBackground(url)
    }
}