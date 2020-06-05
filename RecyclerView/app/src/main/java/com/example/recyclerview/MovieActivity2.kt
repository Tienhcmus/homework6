package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie2.*

class MovieActivity2 : AppCompatActivity() {
//    private var lastStatus = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie2)
        supportActionBar?.hide()
        getAndDisplayData()

    }
    private fun getAndDisplayData() {
        val data = intent.extras

        if (data != null) {
            val title = data.getString("title")
            val rating = data.getFloat("vote_average")
            val release = data.getString("release_date")
            val descrition = data.getString("overview")
            val url = data.getString("poster_path")
//            val lastStatus = data.getBoolean("favorite")

            aa_Title2.text = title
            ratingBar3.rating = rating/2
            rating3.text = rating.toString()
            aa_Releasedate2.text = release
            aa_description2.text = descrition

            Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.loading_image)
                .into(aa_ImageMovie2)
//            favorite_2.isChecked =lastStatus
//            favorite_2.setBackgroundResource( when(favorite_2.isChecked) {
//                true -> R.drawable.heart_24dp
//                else -> R.drawable.ic_not_24dp
//            })
//        }
//        favorite_2.setOnClickListener {
//            if (favorite_2.isChecked) {
//                AlertDialog.Builder(this)
//                    .setMessage("Add to Favourite ?")
//                    .setPositiveButton("ADD") { _, _ ->
//                        favorite_2.isChecked = true
//                        favorite_2.setBackgroundResource(R.drawable.heart_24dp)
//                    }
//                    .setNegativeButton("CANCEL") { _, _ ->
//                        favorite_2.isChecked = true
//                    }
//                    .show()
//            }
//            else {
//                AlertDialog.Builder(this)
//                    .setMessage("Remove from Favourite ?")
//                    .setPositiveButton("REMOVE") { _, _ ->
//                        favorite_2.isChecked = false
//                        favorite_2.setBackgroundResource(R.drawable.ic_not_24dp)
//                    }
//                    .setNegativeButton("CANCEL") { _, _ ->
//                        favorite_2.isChecked = true
//                    }
//                    .show()
//            }
        }

    }
}


