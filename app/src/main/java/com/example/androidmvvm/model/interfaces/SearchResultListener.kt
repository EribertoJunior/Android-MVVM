package com.example.androidmvvm.model.interfaces

interface SearchResultListener<T> {
    fun onSearchResult(result: T)

    fun onSearchErro(mensagem:String)
}