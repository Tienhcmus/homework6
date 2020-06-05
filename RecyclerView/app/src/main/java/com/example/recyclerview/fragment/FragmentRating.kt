package com.example.recyclerview.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

class FragmentRating : Fragment(){
    private lateinit var db: RoomDatabase
    var myMovie2 = ArrayList<Movie>()
    lateinit var dao: FavoriteDAO
    var RatingMovie = ArrayList<Movie>()
    var Count_rating = 1
    var StateFragment = 1
    lateinit var adapter_rating: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = RoomDatabase.invoke(activity as Context)
        val activity: MainActivity = activity as MainActivity
        RatingMovie = activity.getRatingdata()
        initRoomDatabase()
        getMovies()
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return   inflater.inflate(R.layout.rating_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_rating.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = MovieAdapter(context, RatingMovie , listener)
            getPagelist()
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

            rv_rating.apply {
                StateFragment = 1
                layoutManager = LinearLayoutManager(activity)
                adapter = MovieAdapter(context,  RatingMovie, listener)
            }
        }
        if(id == R.id.gridview)
        {
            rv_rating.apply {
                StateFragment = 2
                layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
                adapter = MovieAdapter_Grid(context, RatingMovie, listener2)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private val listener = object : MovieAdapter.MovieListener {
        override fun onClick(pos: Int, movie: Movie) {
            startProfileActivity(movie)
        }
        override fun favrite(pos: Int, movie: Movie) {
            val builder = AlertDialog.Builder(activity)
                .setMessage("Bạn có muốn thêm vào danh sách phim yêu thích không?")
                .setPositiveButton("YES") { _, _ ->
                    if (myMovie2.contains(movie)){
                        Toast.makeText(activity,"Phim đã được thêm vào danh sách yêu thích ", Toast.LENGTH_SHORT).show()

                    }
                    else {
                        movie.favourite = true
                        handleSubmitDatafromRating(movie)
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

        override fun favrite(pos: Int, movie: Movie) {
            val builder = AlertDialog.Builder(activity)
                .setMessage("Bạn có muốn thêm vào danh sách phim yêu thích không?")
                .setPositiveButton("YES") { _, _ ->
                    if (myMovie2.contains(movie)){
                        Toast.makeText(activity,"Phim đã được thêm vào danh sách yêu thích ", Toast.LENGTH_SHORT).show()

                    }
                    else {
                        movie.favourite = true
                        handleSubmitDatafromRating(movie)
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
    private fun handleSubmitDatafromRating(movie: Movie) {
        val movieDAO = db.favoriteDAO()
        movieDAO.insert(movie)
    }
    private fun getNowRatingFromApi(Count : Int) {
        MovieService.getInstance().getApi().getTopRating(Count).enqueue(object : Callback<ListMovie> {
            override fun onFailure(call: Call<ListMovie>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<ListMovie>?, response: Response<ListMovie>?) {
                response?.let {
                    val resp = it.body()
                    RatingMovie = resp.movie
                }
            }
        })

    }
    fun loadmore(){
        rv_rating.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLastItemDisplaying(recyclerView)) {
                    Count_rating++
                    when (StateFragment){
                        1 -> {
                            getNowRatingFromApi(Count_rating)
                            Timer(false).schedule(500) {
                                activity?.runOnUiThread {
                                    Toast.makeText(activity, "Page $Count_rating", Toast.LENGTH_SHORT).show()
                                    rv_rating.apply {
                                        layoutManager = LinearLayoutManager(activity)
                                        adapter = MovieAdapter(context, RatingMovie, listener)

                                    }
                                }
                            }
                        }
                        2-> {
                            getNowRatingFromApi(Count_rating)
                            Timer(false).schedule(500) {
                                activity?.runOnUiThread {
                                    Toast.makeText(activity, "Page $Count_rating", Toast.LENGTH_SHORT).show()
                                    rv_rating.apply {
                                        layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
                                        adapter = MovieAdapter_Grid(context, RatingMovie, listener2)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        })
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
    private fun initRoomDatabase() {
        val db = RoomDatabase.invoke(activity as Context)
        dao = db.favoriteDAO()
    }
    private fun getMovies() {
        val movies = dao.getAll()
        myMovie2.addAll(movies)

    }
    private fun getPagelist() {
        getNowRatingFromApi(Count_rating)
        Handler().postDelayed({
            if (::adapter_rating.isInitialized) {
                adapter_rating.notifyDataSetChanged()
            } else {
                adapter_rating = MovieAdapter(requireActivity(), RatingMovie , listener)
                rv_rating.adapter = adapter_rating
            }
        }, 1000)
    }
}