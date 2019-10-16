package com.example.androidmvvm.model.retrofit.api

import com.example.androidmvvm.model.entidades.Fork
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ForkGithubApi {

    @GET("repos/{criador}/{repositorio}/pulls")
    fun getForks(
        @Path("criador") nomeProprietario: String,
        @Path("repositorio") nomeRepositorio: String
    ): Call<ArrayList<Fork>>
}