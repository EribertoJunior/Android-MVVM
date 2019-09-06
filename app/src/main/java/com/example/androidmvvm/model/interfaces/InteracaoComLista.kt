package com.example.androidmvvm.model.interfaces

interface InteracaoComLista<T> {
    fun buscarmais()
    fun selecionou(itemSelecionado: T)
}