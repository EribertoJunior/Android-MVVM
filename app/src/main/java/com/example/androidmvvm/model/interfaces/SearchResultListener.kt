package com.example.androidmvvm.model.interfaces

import com.example.androidmvvm.model.entidades.RepositorioDTO

interface SearchResultListener {
    fun onSearchResult(result: RepositorioDTO)

    fun onSearchErro(mensagem:String)
}