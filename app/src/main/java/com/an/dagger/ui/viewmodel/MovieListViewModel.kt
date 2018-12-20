package com.an.dagger.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.an.dagger.data.Resource
import com.an.dagger.data.local.dao.MovieDao
import com.an.dagger.data.local.entity.MovieEntity
import com.an.dagger.data.remote.api.MovieApiService
import com.an.dagger.data.repository.MovieRepository
import javax.inject.Inject


/*
 * We are injecting the MovieDao class
 * and the MovieApiService class to the ViewModel.
 * */
class MovieListViewModel @Inject constructor(
        movieDao: MovieDao,
        movieApiService: MovieApiService) : ViewModel() {

    /* You can see we are initialising the MovieRepository class here */
    private val movieRepository: MovieRepository = MovieRepository(movieDao, movieApiService)

    /* We are using LiveData to update the UI with the data changes.
     */
    private val moviesListLiveData = MutableLiveData<Resource<List<MovieEntity>>>()

    /*
     * Method called by UI to fetch movies list
     * */
    fun loadMoreMovies() {
        movieRepository.loadMoviesByType()
                .subscribe { resource -> getMoviesLiveData().postValue(resource) }
    }

    /*
     * LiveData observed by the UI
     * */
    fun getMoviesLiveData() = moviesListLiveData
}
