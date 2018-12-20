package com.an.dagger.data.repository


import com.an.dagger.data.NetworkBoundResource
import com.an.dagger.data.Resource
import com.an.dagger.data.local.dao.MovieDao
import com.an.dagger.data.local.entity.MovieEntity
import com.an.dagger.data.remote.api.MovieApiService
import com.an.dagger.data.remote.model.MovieApiResponse
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Singleton


/*
 * One of the first things we do in the Repository class
 * is to make it a Singleton.
 * */

@Singleton
class MovieRepository(
        private val movieDao: MovieDao,
        private val movieApiService: MovieApiService
) {


    /*
     * We are using this method to fetch the movies list
     * NetworkBoundResource is part of the Android architecture
     * components. You will notice that this is a modified version of
     * that class. That class is based on LiveData but here we are
     * using Observable from RxJava.
     *
     * There are three methods called:
     * a. fetch data from server
     * b. fetch data from local
     * c. save data from api in local
     *
     * So basically we fetch data from server, store it locally
     * and then fetch data from local and update the UI with
     * this data.
     *
     * */
    fun loadMoviesByType(): Observable<Resource<List<MovieEntity>>> {
        return object : NetworkBoundResource<List<MovieEntity>, MovieApiResponse>() {

            override fun saveCallResult(item: MovieApiResponse) {
                movieDao.insertMovies(item.results)
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            override fun loadFromDb(): Flowable<List<MovieEntity>> {
                val movieEntities = movieDao.getMoviesByPage()
                return if (movieEntities == null || movieEntities.isEmpty()) {
                    Flowable.empty()
                } else Flowable.just(movieEntities)
            }

            override fun createCall(): Observable<Resource<MovieApiResponse>> {
                return movieApiService.fetchMoviesByType()
                        .flatMap { movieApiResponse ->
                            Observable.just(
                                    if (movieApiResponse == null) Resource.error("", MovieApiResponse(1, emptyList(), 0, 1))
                                    else Resource.success(movieApiResponse)
                            )
                        }
            }
        }.getAsObservable()
    }
}
