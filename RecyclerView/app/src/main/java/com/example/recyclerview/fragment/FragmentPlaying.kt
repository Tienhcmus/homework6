package com.example.recyclerview.fragment

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.*
import com.example.recyclerview.dataroom.FavoriteDAO
import com.example.recyclerview.dataroom.RoomDatabase
import com.example.recyclerview.movie.ListMovie
import com.example.recyclerview.movie.Movie
import com.example.recyclerview.movie.MovieService
import kotlinx.android.synthetic.main.playing_fragment.*
import kotlinx.android.synthetic.main.rating_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class FragmentPlaying() : Fragment (){

    var myMovie = ArrayList<Movie>()
    private lateinit var db:RoomDatabase
    lateinit var dao: FavoriteDAO
    var PlayingMovie = ArrayList<Movie>()
    var Count = 1
    var StateFragment = 1
    lateinit var adapter_list: MovieAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        initRoomDatabase()
        getMovies()
        db = RoomDatabase.invoke(activity as Context)
        val activity: MainActivity = activity as MainActivity
        PlayingMovie = activity.getPlayingdata()

        Log.e(TAG,"data: ${PlayingMovie}")

        super.onCreate(savedInstanceState)


    }
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.playing_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle? ) {
        super.onViewCreated(view, savedInstanceState)
        rv.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = MovieAdapter(context,  PlayingMovie, listener)
            getPage()
        }
        loadmore()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.listview)
        {

            rv.apply {
                StateFragment = 1
                layoutManager = LinearLayoutManager(activity)
                adapter = MovieAdapter(context,  PlayingMovie, listener)
            }
        }
        if(id == R.id.gridview)
        {
          rv.apply {
              StateFragment = 2
              layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
              adapter = MovieAdapter_Grid(context, PlayingMovie, listener2)
          }
        }
        return super.onOptionsItemSelected(item)
    }
    private var listener = object : MovieAdapter.MovieListener {
        override fun onClick(pos: Int, movie: Movie) {
            startProfileActivity(movie)
        }

        override fun favrite(pos: Int, movies: Movie) {
            val builder = AlertDialog.Builder(activity)
                .setMessage("Bạn có muốn thêm vào danh sách phim yêu thích không?")
                .setPositiveButton("YES") { _, _ ->
                    //add to Favorite movie
                    if (myMovie.contains(movies)){
                        Toast.makeText(activity,"Phim đã được thêm vào danh sách yêu thích ", Toast.LENGTH_SHORT).show()

                    }
                    else {
                        handleSubmitDatafromPlaying(movies)
                        Toast.makeText(activity,"Thêm vào danh sách yêu thích thành công ", Toast.LENGTH_SHORT).show()
                    }

                }
                .setNegativeButton("NO") { dialog, _ -> dialog?.dismiss() }
            val dialog = builder.create();
            dialog.show()
        }
        override fun onItemLongCLicked(pos: Int) {
            TODO("Not yet implemented")
        }
    }

        private var listener2 = object : MovieAdapter_Grid.MovieListener {
            override fun onClick(pos: Int, movie: Movie) {
                startProfileActivity(movie)
            }
            override fun favrite(pos: Int, movies: Movie) {
                val builder = AlertDialog.Builder(activity)
                    .setMessage("Bạn có muốn thêm vào danh sách phim yêu thích không?")
                    .setPositiveButton("YES") { _, _ ->
                            //add to Favorite movie
                        if (myMovie.contains(movies)){
                            Toast.makeText(activity,"Phim đã được thêm vào danh sách yêu thích ", Toast.LENGTH_SHORT).show()

                        }
                        else {
                            handleSubmitDatafromPlaying(movies)
                            Toast.makeText(activity,"Thêm vào danh sách yêu thích thành công ", Toast.LENGTH_SHORT).show()
                        }


                    }
                    .setNegativeButton("NO") { dialog, _ -> dialog?.dismiss() }
                val dialog = builder.create();
                dialog.show()
            }

            override fun onItemLongCLicked(pos: Int) {
                TODO("Not yet implemented")
            }
        }

    private fun startProfileActivity(movie: Movie) {
        val intent = Intent (activity, MovieActivity2::class.java)
        intent.putExtra("title", movie.title)
        intent.putExtra("vote_average", movie.vote_average)
        intent.putExtra("release_date", movie.release_date)
        intent.putExtra("overview", movie.overview)
        intent.putExtra("poster_path", movie.getURL())
        activity?.startActivity(intent)
    }
    // send data to room
    private fun handleSubmitDatafromPlaying(movie: Movie) {
        val movieDAO = db.favoriteDAO()
        movie.favourite = true
        movieDAO.insert(movie)
    }

    private fun initRoomDatabase() {
        val db = RoomDatabase.invoke(activity as Context)
        dao = db.favoriteDAO()
    }
    private fun getMovies() {
        val movies = dao.getAll()
        myMovie.addAll(movies)

    }
    private fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
        if (recyclerView.adapter!!.itemCount != 0) {
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!
                    .getItemCount() - 1
            ) return true
        }
        return false
    }
    private fun getNowPlayingFromApi(Count : Int) {
        MovieService.getInstance().getApi().getNowPlaying(Count).enqueue(object : Callback<ListMovie> {
            override fun onFailure(call: Call<ListMovie>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<ListMovie>?, response: Response<ListMovie>?) {
                response?.let {
                    val resp = it.body()
                    PlayingMovie = resp.movie
                }
            }
        })
    }
    fun loadmore(){
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLastItemDisplaying(recyclerView)) {
                    Count++
                    when (StateFragment){
                        1 -> {
                            getNowPlayingFromApi(Count)
                            Timer(false).schedule(500) {
                            activity?.runOnUiThread {
                                Toast.makeText(activity, "Page $Count", Toast.LENGTH_SHORT).show()
                                rv.apply {
                                    layoutManager = LinearLayoutManager(activity)
                                    adapter = MovieAdapter(context, PlayingMovie, listener)

                                }
                            }
                        }
                        }
                        2-> {
                            getNowPlayingFromApi(Count)
                            Timer(false).schedule(500) {
                                activity?.runOnUiThread {
                                    Toast.makeText(activity, "Page $Count", Toast.LENGTH_SHORT).show()
                                    rv.apply {
                                        layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
                                        adapter = MovieAdapter_Grid(context, PlayingMovie, listener2)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        })
    }
    private fun getPage() {
        getNowPlayingFromApi(Count)
        Handler().postDelayed({
            if (::adapter_list.isInitialized) {
                adapter_list.notifyDataSetChanged()
            } else {
                adapter_list = MovieAdapter(requireActivity(), PlayingMovie , listener)
                rv.adapter = adapter_list
            }
        }, 1000)
    }
}


