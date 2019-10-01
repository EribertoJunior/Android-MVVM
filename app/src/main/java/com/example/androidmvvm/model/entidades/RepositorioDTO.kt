package com.example.androidmvvm.model.entidades

import java.io.Serializable

class RepositorioDTO(
    var items: ArrayList<Repositorio> = arrayListOf(),
    var quantidadeAdicionada: Int = 0,
    var quantidadePorPagina: Int = 0,
    var errorManseger: String = "",
    var proximaPage: Int = 33,
    var status: STATUS,
    var recarga: Boolean = true
) : Serializable {

    enum class STATUS {
        OPEN_LOADING,
        CLOSE_LOADING,
        SUCCESS,
        RECARREGAR,
        ERROR
    }
}