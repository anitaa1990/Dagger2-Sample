package com.an.dagger.ui.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.an.dagger.databinding.MainActivityBinding
import com.an.dagger.ui.adapter.MoviesListAdapter
import com.an.dagger.ui.viewmodel.MovieListViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject
import com.an.dagger.R
import com.an.dagger.data.local.entity.MovieEntity
import com.an.dagger.ui.custom.recyclerview.PagerSnapHelper
import com.an.dagger.ui.custom.recyclerview.RecyclerSnapItemListener

class MainActivity : AppCompatActivity() {


    /*
     * Step 1: Here as mentioned in Step 5, we need to
     * inject the ViewModelFactory. The ViewModelFactory class
     * has a list of ViewModels and will provide
     * the corresponding ViewModel in this activity
     * */
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory


    /*
     * I am using DataBinding
     * */
    private lateinit var binding: MainActivityBinding


    /*
     * This is our ViewModel class
     * */
    lateinit var moviesListViewModel: MovieListViewModel


    private lateinit var moviesListAdapter: MoviesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        /*
         * Step 2: Remember in our ActivityModule, we
         * defined MainActivity injection? So we need
         * to call this method in order to inject the
         * ViewModelFactory into our Activity
         * */
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        initialiseView()
        initialiseViewModel()
    }


    /*
     * Initialising the View using Data Binding
     * */
    private fun initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        moviesListAdapter = MoviesListAdapter(this)
        binding.moviesList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.moviesList.adapter = moviesListAdapter

        val startSnapHelper = PagerSnapHelper(
                object : RecyclerSnapItemListener {
                    override fun onItemSnap(position: Int) {
                        val movie = moviesListAdapter.getItem(position)
                        binding.overlayLayout.updateCurrentBackground(movie.getFormattedPosterPath())
                    }
                }
        )
        startSnapHelper.attachToRecyclerView(binding.moviesList)
    }


    /*
     * Step 3: Initialising the ViewModel class here.
     * We are adding the ViewModelFactory class here.
     * We are observing the LiveData
     * */
    private fun initialiseViewModel() {
        moviesListViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel::class.java)
        moviesListViewModel.getMoviesLiveData().observe(this, Observer { resource ->
            if (resource!!.isLoading) {
                displayLoader()

            } else if (resource.data != null && !resource.data.isEmpty()) {
                updateMoviesList(resource.data)

            } else
                handleErrorResponse()
        })
        /* Fetch movies list  */
        moviesListViewModel.loadMoreMovies()
    }


    private fun displayLoader() {
        binding.moviesList.visibility = View.GONE
        binding.loaderLayout.rootView.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.moviesList.visibility = View.VISIBLE
        binding.loaderLayout.rootView.visibility = View.GONE
    }

    private fun updateMoviesList(movies: List<MovieEntity>) {
        hideLoader()
        binding.emptyLayout.emptyContainer.visibility = View.GONE
        binding.moviesList.visibility = View.VISIBLE
        moviesListAdapter.setItems(movies)
    }

    private fun handleErrorResponse() {
        hideLoader()
        binding.moviesList.visibility = View.GONE
        binding.emptyLayout.emptyContainer.visibility = View.VISIBLE
    }
}