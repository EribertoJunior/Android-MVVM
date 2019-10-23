package com.example.androidmvvm.di.module

import com.example.androidmvvm.model.repository.repository_impl.ForkDataRepository
import com.example.androidmvvm.model.repository.repository_impl.RepoDataRepository
import com.example.androidmvvm.model.retrofit.api.RepositoriosGithubApi
import com.example.androidmvvm.view_model.ForkViewModel
import com.example.androidmvvm.view_model.RepositorioViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    factory<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(get())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder().addInterceptor(get()).build()
    }

    single {
        RepoDataRepository()
    }
    single {
        ForkDataRepository()
    }

    viewModel {
        RepositorioViewModel(get())
    }
    viewModel {
        ForkViewModel(get())
    }
}