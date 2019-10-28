package com.example.androidmvvm.model.repository.repository_impl

import com.example.androidmvvm.model.entidades.Proprietario
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.repository.RepoRepository
import com.example.androidmvvm.model.retrofit.RetrofitConfig
import org.koin.core.KoinComponent
import retrofit2.Response
import retrofit2.Retrofit

class RepoDataRepository(private val retrofit: RetrofitConfig) : RepoRepository, KoinComponent {
    override suspend fun getAll(page: Int): Response<RepositorioDTO> {
        return retrofit.repositorioService().getRepositorios(page)
    }

    override suspend fun getOwner(login: String): Response<Proprietario> {
        return retrofit.proprietarioService().buscarDadosDoProprietario(login)
    }
}