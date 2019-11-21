package com.example.androidmvvm.model.repository.repository_impl

import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.Proprietario
import com.example.androidmvvm.model.repository.ForkRepository
import com.example.androidmvvm.model.retrofit.RetrofitConfig
import retrofit2.Response

class ForkDataRepository(private val retrofitConfig: RetrofitConfig) : ForkRepository {
    override suspend fun getForks(
        nomeProprietario: String,
        nomeRepositorio: String,
        page: Int
    ): Response<ArrayList<Fork>> {
        return retrofitConfig.forkService().getForks(nomeProprietario, nomeRepositorio, page)
    }

    override suspend fun getOwner(login: String): Response<Proprietario> {
        return retrofitConfig.proprietarioService().buscarDadosDoProprietario(login)
    }
}