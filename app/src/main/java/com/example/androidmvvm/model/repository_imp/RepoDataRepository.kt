package com.example.androidmvvm.model.repository_imp

import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.repository.RepoRepository
import com.example.androidmvvm.model.retrofit.RetrofitConfig
import retrofit2.Response

class RepoDataRepository : RepoRepository {
    override suspend fun getAll(page: Int): Response<RepositorioDTO> {
        return RetrofitConfig().repositorioService().getRepositorios(page = page)
    }
}