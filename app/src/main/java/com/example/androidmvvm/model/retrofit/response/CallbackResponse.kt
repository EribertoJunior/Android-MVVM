package com.example.androidmvvm.model.retrofit.response

interface CallbackResponse<T> {
    fun success(response: T)
    fun failure(response: String?)
}