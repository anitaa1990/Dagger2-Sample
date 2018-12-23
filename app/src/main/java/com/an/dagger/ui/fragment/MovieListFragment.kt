package com.an.dagger.ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.an.dagger.R
import com.an.dagger.data.local.entity.MovieEntity
import com.an.dagger.databinding.MovieFragmentBinding
import com.an.dagger.ui.activity.MainActivity
import com.an.dagger.ui.adapter.MoviesListAdapter
import com.an.dagger.ui.custom.recyclerview.PagerSnapHelper
import com.an.dagger.ui.custom.recyclerview.RecyclerSnapItemListener
import com.an.dagger.ui.viewmodel.MovieListViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MovieListFragment : Fragment() {


    /*
     * Step 1: Here, we need to inject the ViewModelFactory.
     * */
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    /*
 * I am using DataBinding
 * */
    private lateinit var binding: MovieFragmentBinding


    /*
     * This is our ViewModel class
     * */
    lateinit var moviesListViewModel: MovieListViewModel


    private lateinit var moviesListAdapter: MoviesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * Step 2: Remember in our FragmentModule, we
         * defined MovieListFragment injection? So we need
         * to call this method in order to inject the
         * ViewModelFactory into our Fragment
         * */
        AndroidSupportInjection.inject(this)
        initialiseViewModel()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseView()
    }

    private fun initialiseView() {
        moviesListAdapter = MoviesListAdapter(requireActivity())
        binding.moviesList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.moviesList.adapter = moviesListAdapter

        val startSnapHelper = PagerSnapHelper(
                object : RecyclerSnapItemListener {
                    override fun onItemSnap(position: Int) {
                        val movie = moviesListAdapter.getItem(position)
                        (requireActivity() as MainActivity).updateBackground(movie.getFormattedPosterPath())
                    }
                }
        )
        startSnapHelper.attachToRecyclerView(binding.moviesList)
    }


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
