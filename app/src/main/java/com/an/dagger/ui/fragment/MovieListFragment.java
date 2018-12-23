package com.an.dagger.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.an.dagger.R;
import com.an.dagger.data.local.entity.MovieEntity;
import com.an.dagger.databinding.MovieFragmentBinding;
import com.an.dagger.factory.ViewModelFactory;
import com.an.dagger.ui.activity.MainActivity;
import com.an.dagger.ui.adapter.MoviesListAdapter;
import com.an.dagger.ui.custom.recyclerview.PagerSnapHelper;
import com.an.dagger.ui.viewmodel.MovieListViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class MovieListFragment extends Fragment {

    /*
     * Step 1: Here, we need to inject the ViewModelFactory.
     * */
    @Inject
    ViewModelFactory viewModelFactory;

    /*
     * I am using DataBinding
     * */
    private MovieFragmentBinding binding;

    /*
     * This is our ViewModel class
     * */
    MovieListViewModel movieListViewModel;

    private MoviesListAdapter moviesListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Step 2: Remember in our FragmentModule, we
         * defined MovieListFragment injection? So we need
         * to call this method in order to inject the
         * ViewModelFactory into our Fragment
         * */
        AndroidSupportInjection.inject(this);
        initialiseViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseView();
    }

    private void initialiseView() {
        moviesListAdapter = new MoviesListAdapter(getActivity());
        binding.moviesList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.moviesList.setAdapter(moviesListAdapter);

        /* SnapHelper to change the background of the activity based on the list item
         * currently visible */
        SnapHelper startSnapHelper = new PagerSnapHelper(position -> {
            MovieEntity movie = moviesListAdapter.getItem(position);
            ((MainActivity)getActivity()).updateBackground(movie.getPosterPath());
        });
        startSnapHelper.attachToRecyclerView(binding.moviesList);
    }


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
    }

    private void hideLoader() {
        binding.moviesList.setVisibility(View.VISIBLE);
        binding.loaderLayout.rootView.setVisibility(View.GONE);
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