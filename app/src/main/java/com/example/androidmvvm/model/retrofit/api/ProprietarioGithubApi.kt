package com.example.androidmvvm.model.retrofit.api

import com.example.androidmvvm.model.entidades.Proprietario
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProprietarioGithubApi {
    @GET("users/{login}")
    suspend fun buscarDadosDoProprietario(
        @Path("login") login: String
    ): Response<Proprietario>
}