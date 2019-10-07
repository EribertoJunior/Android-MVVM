package com.example.androidmvvm.model.interfaces

import com.example.androidmvvm.model.entidades.RepositorioDTO

interface SearchResultListener<T> {
    fun onSearchResult(result: T)

    fun onSearchErro(mensagem:String)
}