package com.example.recyclerview.movie


import com.google.gson.annotations.SerializedName

data class ListMovie(
    val page: Int,
    @SerializedName("total_results") val totalResult: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val movie: ArrayList<com.example.recyclerview.movie.Movie>
)