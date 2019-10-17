package com.example.androidmvvm.model.repository.repository_impl

import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.repository.ForkRepository
import com.example.androidmvvm.model.retrofit.RetrofitConfig
import retrofit2.Response

class ForkDataRepository: ForkRepository {

    override suspend fun getForks(
        nomeProprietario: String,
        nomeRepositorio: String,
        page:Int
    ): Response<ArrayList<Fork>> {
        return RetrofitConfig().forkService().getForks(nomeProprietario, nomeRepositorio, page)
    }
}