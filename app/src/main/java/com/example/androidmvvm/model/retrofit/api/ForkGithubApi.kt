package com.example.androidmvvm.model.retrofit.api

import com.example.androidmvvm.model.entidades.Fork
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ForkGithubApi {

    @GET("repos/{criador}/{repositorio}/pulls")
    suspend fun getForks(
        @Path("criador") nomeProprietario: String,
        @Path("repositorio") nomeRepositorio: String,
        @Query("page") page: Int
    ): Response<ArrayList<Fork>>
}