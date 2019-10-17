package com.example.androidmvvm.model.repository

import com.example.androidmvvm.model.entidades.Fork
import retrofit2.Response

interface ForkRepository {
    suspend fun getForks(
        nomeProprietario: String,
        nomeRepositorio: String,
        page: Int
    ): Response<ArrayList<Fork>>
}