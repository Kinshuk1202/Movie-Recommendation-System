package com.kinshuk.movie_recommendation_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.kinshuk.movie_recommendation_app.databinding.ActivityMainBinding
import com.kinshuk.movie_recommendation_app.models.Info
import com.kinshuk.movie_recommendation_app.retrofit.RetrofitInstance
import com.kinshuk.movie_recommendation_app.retrofit.RetrofitInterface
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cardAdapter: CardAdapter
    private lateinit var movies_list:MutableList<Info>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        movies_list = mutableListOf()
        binding.getBtn.setOnClickListener {
            val movie_name = binding.editText.text.toString()
            if(movie_name != null) {
                movies_list.clear()
                val retrofitService = RetrofitInstance.getRetrofitInstance()
                    .create(RetrofitInterface::class.java)
                lifecycleScope.launch {
                    val response = retrofitService.getData(movie_name)
                    if (response.isSuccessful && response.body() != null) {
                        val recommendations = response.body()!!.recommendations
                        if (recommendations != null) {
                            for (info in recommendations) {
                                movies_list.add(info)
                            }
                        }
                    }
                    if(movies_list.size>0)
                    {
                        setCards()
                    }
                    else
                        Toast.makeText(applicationContext,"Movie Not Found",Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
    private fun setCards()
    {
        cardAdapter = CardAdapter(movies_list)
        binding.cards.apply {
            layoutManager = StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = cardAdapter
        }
    }
}