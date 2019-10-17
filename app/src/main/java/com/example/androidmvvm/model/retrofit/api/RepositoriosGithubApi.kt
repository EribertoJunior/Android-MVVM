package com.example.androidmvvm.model.retrofit.api

import com.example.androidmvvm.model.entidades.RepositorioDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoriosGithubApi {
    @GET("search/repositories?q=language:Java&sort=stars")//"search/repositories?q=language:Java&sort=stars&page=1"
    suspend fun getRepositorios(
        @Query("page") page: Int
    ): Response<RepositorioDTO>

    @GET("users/")
    suspend fun buscarDadosDoProprietario(

    )
}