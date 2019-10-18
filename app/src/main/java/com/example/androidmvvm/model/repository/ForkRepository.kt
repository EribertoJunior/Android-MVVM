package com.example.androidmvvm.model.repository

import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.Proprietario
import com.example.androidmvvm.model.repository.utils.UtilRepository
import retrofit2.Response

interface ForkRepository : UtilRepository {

    suspend fun getForks(nomeProprietario: String,nomeRepositorio: String,page: Int): Response<ArrayList<Fork>>
    override suspend fun getOwner(login: String): Response<Proprietario>
}