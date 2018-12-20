package com.an.dagger.data.local.dao

import android.arch.persistence.room.*
import com.an.dagger.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    /* Method to insert the movies fetched from api
     * to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<MovieEntity>): LongArray

    /* Method to fetch the movies stored locally */
    @Query("SELECT * FROM `MovieEntity`")
    fun getMoviesByPage(): List<MovieEntity>
}