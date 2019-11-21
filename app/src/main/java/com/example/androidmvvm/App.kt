package com.example.androidmvvm

import android.app.Application
import com.example.androidmvvm.di.setUpDI

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setUpDI(BuildConfig.BASE_URL)
    }
}