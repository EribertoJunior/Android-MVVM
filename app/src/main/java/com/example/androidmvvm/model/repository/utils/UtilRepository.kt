package com.example.androidmvvm.model.repository.utils

import com.example.androidmvvm.model.entidades.Proprietario
import retrofit2.Response

interface UtilRepository {
    suspend fun getOwner(login: String): Response<Proprietario>
}