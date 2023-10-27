package edu.vt.cs3714.retrofitrecyclerviewguide

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MovieItemRepository(private val movieDao: MovieItemDao) {

    val allMovies: LiveData<List<MovieItem>> = movieDao.getAllMovies()

    @WorkerThread
    fun insert(movie: MovieItem) {


        movieDao.insertMovie(movie)
    }

    @WorkerThread
    fun deleteAll() {
        movieDao.deleteAll()
    }
}