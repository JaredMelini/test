package edu.vt.cs3714.retrofitrecyclerviewguide

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.vt.cs3714.retrofitrecyclerviewguide.R
import edu.vt.cs3714.retrofitrecyclerviewguide.databinding.ActivityMainBinding
import edu.vt.cs3714.retrofitrecyclerviewguide.databinding.CardViewBinding
import kotlinx.coroutines.*
import retrofit2.HttpException

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter : MovieListAdapter
    //emulating a repository; this is a list of movieDB movie IDs.
    private val nowPlaying = mutableListOf(
        424,
        11031,
        8005,
        154,
        11,
        238,
        426563,
        463684,
        505192,
        568709,
        491854,
        398173,
        445629
    )

    private val movies = ArrayList<MovieItem>()
    private lateinit var job: Job

    //TODO: add your own key. Hint: https://developers.themoviedb.org/3/getting-started/introduction
    //Make sure to put your API key in /res/values/strings.xml
    //Need this to be lazy, because the reference to R.string.api_key doesn't exist until AFTER
    //onCreate() is executed. By the time we need the apiKey, R.string.api_key exists.
    private val apiKey by lazy {
        resources.getString(R.string.api_key)
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        adapter.filter(query)
        return true
    }

    //Creates a handle to the Retrofit Service pointing to the MovieDB URL.
    private val retrofitService by lazy {
        RetrofitService.create(resources.getString(R.string.base_url))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieListAdapter()
        binding.movieList.adapter = adapter
        binding.movieList.layoutManager = LinearLayoutManager(this)

        val model = ViewModelProvider(this)[MovieViewModel::class.java]

        model.allMovies.observe(
            this,
            Observer<List<MovieItem>> { movies ->
                movies?.let {
                    adapter.setMovies(it)
                }
            }
        )

        binding.refresh.setOnClickListener {
            model.refreshMovies(1)
        }
    }

    /**
     * Make sure to cancel the job when onDestroy() is called.
     */
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()

    }

    inner class MovieListAdapter :
        RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

        private var movies = emptyList<MovieItem>()
        private var allMovies = emptyList<MovieItem>() // All movies without filtering
        private var displayedMovies = allMovies // Movies currently displayed in the RecyclerView
        private var moviesBackup= emptyList<MovieItem>()

        internal fun setMovies(movies: List<MovieItem>) {
            moviesBackup = movies
            this.movies = movies
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return movies.size
        }

        fun filter(query: String?) {
            movies = movies.filter{it.title.contains(query!!)}
            notifyDataSetChanged()
        }

        fun restore(){

            movies = moviesBackup
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val binding = CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MovieViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            Glide.with(this@MainActivity)
                .load(getString(R.string.picture_base_url) + movies[position].poster_path)
                .apply(RequestOptions().override(128, 128))
                .into(holder.binding.poster)

            holder.binding.title.text = movies[position].title
            holder.binding.rating.text = movies[position].vote_average.toString()
        }

        inner class MovieViewHolder(val binding: CardViewBinding) : RecyclerView.ViewHolder(binding.root),
            View.OnClickListener {
            init {
                binding.root.setOnClickListener(this)
            }

            override fun onClick(view: View?) {
                // Handle item click
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)

        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                adapter.restore()
                return true
            }
        })


        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(this)

        return true
    }



}