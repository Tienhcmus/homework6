package com.example.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerview.fragment.FragmentRating
import com.example.recyclerview.movie.ListMovie
import com.example.recyclerview.movie.Movie


class MovieAdapter(val ctx: Context, private var data: ArrayList<Movie>, val listener: MovieListener ): RecyclerView.Adapter<MovieAdapter.MovieVH>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
            val layoutInflater = LayoutInflater.from(parent.context)

            return MovieVH(layoutInflater, parent)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: MovieVH, position: Int) {
            val movie =  data[position]

            holder.tvTitle.text = movie.title
            holder.tvRating.rating = (movie.vote_average /2).toFloat()
            holder.tvScore.text = movie.vote_average.toString()
            holder.tvRelease.text = movie.release_date
            Glide.with(ctx)
                .load(movie.getURL())
                .centerCrop()
                .into(holder.ivImage)
            holder.itemView.setOnClickListener{
                listener?.onClick(position, movie)
            }
            holder.ivfavorite.setOnClickListener {
                listener?.favrite(position, movie)
            }
            holder.itemView.setOnLongClickListener {
                listener.onItemLongCLicked(position)
                true
            }
        }

        class MovieVH(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(R.layout.movie_item, parent, false)){
        val tvTitle = itemView.findViewById<TextView>(R.id.tv)
        val tvRating = itemView.findViewById<RatingBar>(R.id.ratingBar)
        val tvScore = itemView.findViewById<TextView>(R.id.rating)
        val tvRelease = itemView.findViewById<TextView>(R.id.date)
        val ivImage = itemView.findViewById<ImageView>(R.id.ig)
        var ivfavorite = itemView.findViewById<ImageView>(R.id.favorite)
        }
        interface MovieListener {
            fun onClick(pos: Int, movie: Movie)
            fun favrite(pos: Int, movie: Movie)
            fun onItemLongCLicked(pos: Int)
        }
}