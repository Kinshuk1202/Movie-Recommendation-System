package com.kinshuk.movie_recommendation_app


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kinshuk.movie_recommendation_app.databinding.CardBinding
import com.kinshuk.movie_recommendation_app.models.Info
import com.kinshuk.movie_recommendation_app.retrofit.PosterInterface
import com.kinshuk.movie_recommendation_app.retrofit.RetPoster
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class CardAdapter(private val movies: MutableList<Info>):RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    inner class CardViewHolder(val itemBinding: CardBinding):RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(info: Info) {
            itemBinding.title.text = info.title
            val id = info.id
            val posterRef = RetPoster.getRetrofitInstance().create(PosterInterface::class.java)
            GlobalScope.launch {
                try {
                    val res = posterRef.getMoiveData(id)
                    if (res.isSuccessful && res.body() != null) {
//                        Log.d("KK", "https://image.tmdb.org/t/p/original/"+res.body()!!.poster_path)
                        val url =  "https://image.tmdb.org/t/p/original/"+res.body()!!.poster_path
                        val uri = url.toUri().buildUpon().scheme("https").build()
                        launch(Dispatchers.Main) {
//                            Picasso.get().load(url).resize(200,200).into(itemBinding.poster)
                            Glide.with(itemView.context)
                                .load(uri)
                                .override(200,200)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(itemBinding.poster)
                        }.join()
                    }
                } catch (e: Exception) {
                    Log.e("KK", "Error occurred: ${e.message}", e)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:CardBinding = CardBinding.inflate(layoutInflater)
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
       holder.bind(movies[position])

    }


}