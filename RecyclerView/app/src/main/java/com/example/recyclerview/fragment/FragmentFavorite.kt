package com.example.recyclerview.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.*
import com.example.recyclerview.dataroom.FavoriteDAO
import com.example.recyclerview.dataroom.RoomDatabase
import com.example.recyclerview.movie.Movie
import kotlinx.android.synthetic.main.favorite_fragment.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class FragmentFavorite : Fragment() {
//    var myMovie: java.util.ArrayList<MovieModel> = java.util.ArrayList()
    var myMovie = ArrayList<Movie>()
    lateinit var dao: FavoriteDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var myMovie = ArrayList<Movie>()
        initRoomDatabase()
        getMovies()
        setHasOptionsMenu(true)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_favorite.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = MovieAdapter(context, myMovie , listener)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.listview)
        {
            rv_favorite.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = MovieAdapter(context, myMovie, listener)
            }
        }
        if(id == R.id.gridview)
        {
            rv_favorite.apply {
                layoutManager = GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)
                adapter = MovieAdapter_Grid(context, myMovie, listener2)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private val listener = object : MovieAdapter.MovieListener {
        override fun onClick(pos: Int, movie: Movie) {
            startProfileActivity(movie)
        }

        override fun favrite(pos: Int, movies: Movie) {
        }

        override fun onItemLongCLicked(pos: Int) {
            val builder = AlertDialog.Builder(activity as Context)
            builder
                .setMessage("Bạn muốn xóa phim: ${myMovie[pos].title} ?")
                .setPositiveButton("OK") { _, _ ->
                    DeleteMovie(pos)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ -> dialog?.dismiss() }

            val myDialog = builder.create();
            myDialog.show()
        }
    }



    private var listener2 = object : MovieAdapter_Grid.MovieListener {

        override fun onClick(pos: Int, movie: Movie) {
            TODO("Not yet implemented")
        }

        override fun favrite(pos: Int, movie: Movie) {
        }

        override fun onItemLongCLicked(pos: Int) {
            val builder = AlertDialog.Builder(activity as Context)
            builder
                .setMessage("Bạn muốn xóa phim: ${myMovie[pos].title} ?")
                .setPositiveButton("OK") { _, _ ->
                    DeleteMovie_Grid(pos)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ -> dialog?.dismiss() }

            val myDialog = builder.create();
            myDialog.show()
        }
    }

// Start Profile of Movie
    private fun startProfileActivity(movie: Movie) {
        val intent = Intent (activity, MovieActivity2::class.java)
        intent.putExtra("title", movie.title)
        intent.putExtra("vote_average", movie.vote_average)
        intent.putExtra("release_date", movie.release_date)
        intent.putExtra("overview", movie.overview)
        intent.putExtra("poster_path", movie.getURL())
        activity?.startActivity(intent)
    }

    private fun initRoomDatabase() {
        val db = RoomDatabase.invoke(activity as Context)
        dao = db.favoriteDAO()
    }
    private fun getMovies() {
        val movies = dao.getAll()
        myMovie.addAll(movies)

    }

    //delete movie when Listview
    private fun DeleteMovie(position: Int) {
        dao.delete(myMovie[position]) // remove from Room database  //
        myMovie.removeAt(position) // remove student list on RAM
        Timer(false).schedule(500) {
            activity?.runOnUiThread {
                rv_favorite.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = MovieAdapter(context, myMovie, listener)
                }
            }
        }
    }
    //delete movie when Gridview
    private fun DeleteMovie_Grid(position: Int) {
        dao.delete(myMovie[position]) // remove from Room database  //
        myMovie.removeAt(position) // remove student list on RAM
        Timer(false).schedule(500) {
            activity?.runOnUiThread {
                rv_favorite.apply {
                    layoutManager = GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)
                    adapter = MovieAdapter_Grid(context, myMovie, listener2)
                }
            }
        }
    }
}

