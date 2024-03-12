package com.kinshuk.movie_recommendation_app.retrofit

import com.kinshuk.movie_recommendation_app.models.recommendations
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInterface {

    @GET("/get/{movie}")
    suspend fun getData(@Path("movie") movie_name: String) : Response<recommendations>
}