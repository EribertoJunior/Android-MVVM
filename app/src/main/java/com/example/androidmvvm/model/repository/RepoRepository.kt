package com.example.androidmvvm.model.repository

import com.example.androidmvvm.model.entidades.Proprietario
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.repository.utils.UtilRepository
import retrofit2.Response

interface RepoRepository : UtilRepository {

    suspend fun getAll(page: Int): Response<RepositorioDTO>
    override suspend fun getOwner(login: String): Response<Proprietario>
}