package com.example.recyclerview

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.dataroom.RoomDatabase
import com.example.recyclerview.fragment.FragmentFavorite
import com.example.recyclerview.fragment.FragmentPlaying
import com.example.recyclerview.fragment.FragmentRating
import com.example.recyclerview.movie.ListMovie
import com.example.recyclerview.movie.Movie
import com.example.recyclerview.movie.MovieService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.playing_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    var Fragment_state = 1
    var PlayingMovie = ArrayList<Movie>()
    var RatingMovie = ArrayList<Movie>()
    private val PREFERENCE_NAME = "SETTING_FILES"
    private lateinit var db: RoomDatabase
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get Room Database instance
        db = RoomDatabase.invoke(this)
        //get movie from API
        getNowPlayingFromApi()
        getNowRatingFromApi()

        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //set first view is Fragment Playing
        when(getSharePreference()) {
            1 -> {
                val fragment = FragmentPlaying()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .addToBackStack(null)
                    .commit()
                navView.menu.findItem(R.id.navigation_playing).isChecked = true
            }
            2 -> {
                val fragment = FragmentRating()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .addToBackStack(null)
                    .commit()
                navView.menu.findItem(R.id.navigation_top_rating).isChecked = true
            }
            3 -> {
                val fragment = FragmentFavorite()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .addToBackStack(null)
                    .commit()
                navView.menu.findItem(R.id.navigation_favorite).isChecked = true
            }
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_playing -> {
                    Fragment_state = 1
                    val fragment = FragmentPlaying()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .addToBackStack(null)
                        .commit()
                    saveSharedPreference(Fragment_state)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_top_rating -> {
                    Fragment_state = 2
                    val fragment = FragmentRating()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .addToBackStack(null)
                        .commit()
                    saveSharedPreference(Fragment_state)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favorite -> {
                    Fragment_state = 3
                    val fragment = FragmentFavorite()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .addToBackStack(null)
                        .commit()
                    saveSharedPreference(Fragment_state)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    private  fun saveSharedPreference(int : Int) {
        val preference = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val prefEditor = preference.edit()
        prefEditor.putInt("state", int)
        prefEditor.apply()
    }
    //get state before user kill app
    private fun getSharePreference(): Int{
        val preference = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preference.getInt("state", 2)
    }

    private fun getNowPlayingFromApi() {
        MovieService.getInstance().getApi().getNowPlaying(1).enqueue(object : Callback<ListMovie> {
            override fun onFailure(call: Call<ListMovie>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<ListMovie>?, response: Response<ListMovie>?) {
                response?.let {
                    var resp = it.body()
                    PlayingMovie = resp.movie
                }
            }
        })

    }
    private fun getNowRatingFromApi() {
        MovieService.getInstance().getApi().getTopRating(1).enqueue(object : Callback<ListMovie> {
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
    fun getPlayingdata (): ArrayList<Movie>{
        return PlayingMovie
    }
    fun getRatingdata() : ArrayList<Movie> {
        return RatingMovie
    }
}




