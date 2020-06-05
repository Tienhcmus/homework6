package com.example.recyclerview.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


@TypeConverters(Converters::class)
@Entity(tableName = "movie_table")
data class Movie(
    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
//    @SerializedName("genre_ids") val genreIds: List<Int>,
    @PrimaryKey(autoGenerate = false) val id: Int,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("release_date") val release_date: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average") val vote_average: Double,
    @SerializedName("vote_count") val voteCount: Int,
    var favourite : Boolean
) {
    fun getURL(): String {
        var url:String = "https://image.tmdb.org/t/p/w500/"
        url = url.plus(poster_path)
        return url
    }
}
class Converters {
    @TypeConverter
    fun listToJson(value: List<Int>?) = Gson().toJson(value)!!

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()

}