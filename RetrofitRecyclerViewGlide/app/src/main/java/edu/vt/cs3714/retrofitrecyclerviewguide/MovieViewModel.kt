package edu.vt.cs3714.retrofitrecyclerviewguide

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext



class MovieViewModel (application : Application) : AndroidViewModel(application){
    //Update this with your API KEY
    private val api_key = "169c66cdd5c7baf438f4865ddc403ca0"
    private val api_base_url = "https://api.themoviedb.org/3/"

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    private var disposable: Disposable? = null

    private val repository: MovieItemRepository
    val allMovies: LiveData<List<MovieItem>>

    init {
        val moviesDao = MovieRoomDatabase.getDatabase(application).movieDao()

        repository = MovieItemRepository(moviesDao)
        allMovies = repository.allMovies
    }

    fun refreshMovies(page: Int){
        disposable =
            RetrofitService.create("https://api.themoviedb.org/3/").getNowPlaying(api_key,page).subscribeOn(
                Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                {result -> showResult(result)},
                {error -> showError(error)})
    }

    private fun showError(error: Throwable?) {
        Log.d("t04","Error:"+error?.toString())
    }

    private fun showResult(result: Movies?) {

        Log.d("T04","Page:"+result?.page+"Result:"+result?.results?.last()?.release_date+ " pages "+ result?.total_pages)
        deleteAll()

        result?.results?.forEach { movie ->
            insert(movie)
        }
    }

    private fun insert(movie: MovieItem) = scope.launch(Dispatchers.IO) {
        repository.insert(movie)
    }

    private fun deleteAll() = scope.launch (Dispatchers.IO){
        repository.deleteAll()
    }
}
