package com.example.androidmvvm.model.repository

import com.example.androidmvvm.model.entidades.RepositorioDTO
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface RepoRepository {

    suspend fun getAll(page: Int): Response<RepositorioDTO>
}