package com.example.androidmvvm.model.retrofit.services

import com.example.androidmvvm.model.entidades.RepositorioDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoriosService {
    @GET("search/repositories?q=language:Java&sort=stars")//"search/repositories?q=language:Java&sort=stars&page=1"
    fun getRepositorios(
        @Query("page") page: Int
    ): Call<RepositorioDTO>
}