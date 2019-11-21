package com.example.androidmvvm.di

import android.app.Application
import com.example.androidmvvm.di.module.PROPERTIES_BASE_URL
import com.example.androidmvvm.di.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun Application.setUpDI(baseUrl: String){

    startKoin {
        androidLogger()
        androidContext(this@setUpDI)

        properties(
            mapOf(
                    PROPERTIES_BASE_URL to baseUrl
            )
        )

        modules(
            /**
             * Lista de modulos onde são declaradas as dependências
             * para novos modulos, criá-los em `modules/` e registrá-los abaixo
             */
            listOf(
                appModule
            )
        )

    }
}