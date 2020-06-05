package com.example.recyclerview.dataroom

import androidx.room.*
import com.example.recyclerview.movie.Movie


@Dao
interface FavoriteDAO {
    @Query("SELECT * FROM movie_table")
    fun getAll(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie): Long

    @Delete
    fun delete(movie: Movie)
}