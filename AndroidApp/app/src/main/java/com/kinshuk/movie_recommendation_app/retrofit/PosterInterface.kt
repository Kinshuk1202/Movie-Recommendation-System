package com.kinshuk.movie_recommendation_app.retrofit

import com.kinshuk.movie_recommendation_app.models.movieIdInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PosterInterface {

    @GET("/3/movie/{id}?api_key=1b6d6731870d0371c06f4acd68d55c15")
    suspend fun getMoiveData(@Path("id") id: Int): Response<movieIdInfo>
}