package com.example.androidmvvm.model.retrofit

import com.example.androidmvvm.model.retrofit.api.ForkGithubApi
import com.example.androidmvvm.model.retrofit.api.ProprietarioGithubApi
import com.example.androidmvvm.model.retrofit.api.RepositoriosGithubApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig(baseUrl: String) {
    var interceptador : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptador).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun repositorioService() = retrofit.create(RepositoriosGithubApi::class.java)

    fun proprietarioService() = retrofit.create(ProprietarioGithubApi::class.java)

    fun forkService() = retrofit.create(ForkGithubApi::class.java)
}