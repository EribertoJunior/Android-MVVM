package com.example.androidmvvm.model.retrofit.services

import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.ForkDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ForkService {

    @GET("repos/{criador}/{repositorio}/pulls")
    fun getForks(
        @Path("criador") nomeProprietario: String,
        @Path("repositorio") nomeRepositorio: String
    ): Call<ArrayList<Fork>>
}