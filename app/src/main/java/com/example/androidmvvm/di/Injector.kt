package com.example.androidmvvm.di

import android.app.Application
import com.example.androidmvvm.di.module.netModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun Application.setUpDI(){

    startKoin {
        androidLogger()
        androidContext(this@setUpDI)

        properties(
            mapOf(

            )
        )

        modules(
            /**
             * Lista de modulos onde são declaradas as dependências
             * para novos modulos, criá-los em `modules/` e registrá-los abaixo
             */
            listOf(
                netModule
            )
        )

    }
}