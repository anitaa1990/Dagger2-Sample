package com.an.dagger.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import com.an.dagger.R;
import com.an.dagger.data.local.entity.MovieEntity;
import com.an.dagger.databinding.MainActivityBinding;
import com.an.dagger.factory.ViewModelFactory;
import com.an.dagger.ui.adapter.MoviesListAdapter;
import com.an.dagger.ui.custom.recyclerview.PagerSnapHelper;
import com.an.dagger.ui.viewmodel.MovieListViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    /*
     * Step 1: Here as mentioned in Step 5, we need to
     * inject the ViewModelFactory. The ViewModelFactory class
     * has a list of ViewModels and will provide
     * the corresponding ViewModel in this activity
     * */
    @Inject
    ViewModelFactory viewModelFactory;

    /*
     * I am using DataBinding
     * */
    private MainActivityBinding binding;

    /*
     * This is our ViewModel class
     * */
    private MovieListViewModel movieListViewModel;

    private MoviesListAdapter moviesListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /*
         * Step 2: Remember in our ActivityModule, we
         * defined MainActivity injection? So we need
         * to call this method in order to inject the
         * ViewModelFactory into our Activity
         * */
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        initialiseView();
        initialiseViewModel();
    }

    /*
     * Initialising the View using Data Binding
     * */
    private void initialiseView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        moviesListAdapter = new MoviesListAdapter(this);
        binding.moviesList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.moviesList.setAdapter(moviesListAdapter);

        /* SnapHelper to change the background of the activity based on the list item
         * currently visible */
        SnapHelper startSnapHelper = new PagerSnapHelper(position -> {
            MovieEntity movie = moviesListAdapter.getItem(position);
            binding.overlayLayout.updateCurrentBackground(movie.getPosterPath());
        });
        startSnapHelper.attachToRecyclerView(binding.moviesList);
    }


    /*
     * Step 3: Initialising the ViewModel class here.
     * We are adding the ViewModelFactory class here.
     * We are observing the LiveData
     * */
    private void initialiseViewModel() {
        movieListViewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel.class);
        movieListViewModel.getMoviesLiveData().observe(this, resource -> {
            if(resource.isLoading()) {
                displayLoader();

            } else if(!resource.data.isEmpty()) {
                updateMoviesList(resource.data);

            } else handleErrorResponse();
        });

        /* Fetch movies list  */
        movieListViewModel.loadMoreMovies();
    }

    private void displayLoader() {
        binding.moviesList.setVisibility(View.GONE);
        binding.loaderLayout.rootView.setVisibility(View.VISIBLE);
        binding.loaderLayout.loader.start();
    }

    private void hideLoader() {
        binding.moviesList.setVisibility(View.VISIBLE);
        binding.loaderLayout.rootView.setVisibility(View.GONE);
        binding.loaderLayout.loader.stop();
    }

    private void updateMoviesList(List<MovieEntity> movies) {
        hideLoader();
        binding.emptyLayout.emptyContainer.setVisibility(View.GONE);
        binding.moviesList.setVisibility(View.VISIBLE);
        moviesListAdapter.setItems(movies);
    }


    private void handleErrorResponse() {
        hideLoader();
        binding.moviesList.setVisibility(View.GONE);
        binding.emptyLayout.emptyContainer.setVisibility(View.VISIBLE);
    }
}
