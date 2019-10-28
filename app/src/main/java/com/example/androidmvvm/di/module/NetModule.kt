package com.example.androidmvvm.di.module

import com.example.androidmvvm.model.repository.repository_impl.ForkDataRepository
import com.example.androidmvvm.model.repository.repository_impl.RepoDataRepository
import com.example.androidmvvm.model.retrofit.RetrofitConfig
import com.example.androidmvvm.view_model.ForkViewModel
import com.example.androidmvvm.view_model.RepositorioViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PROPERTIES_BASE_URL = "BASE_URL"

val appModule = module {

    factory{
        val baseUrl = getProperty<String>(PROPERTIES_BASE_URL)
        RetrofitConfig(baseUrl)
    }

    factory<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    factory<OkHttpClient> {
        OkHttpClient.Builder().addInterceptor(get<HttpLoggingInterceptor>()).build()
    }

    single{
        RepoDataRepository(get())
    }
    single {
        ForkDataRepository(get())
    }

    viewModel {
        RepositorioViewModel(get())
    }
    viewModel {
        ForkViewModel(get())
    }
}