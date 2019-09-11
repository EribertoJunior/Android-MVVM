package com.example.androidmvvm.model.entidades

import java.io.Serializable

class RepositorioDTO(
    var items: ArrayList<Repositorio> = arrayListOf(),
    var errorManseger: String = "",
    var proximaPage: Int = 1,
    var status: STATUS
) : Serializable {

    enum class STATUS {
        OPEN_LOADING,
        CLOSE_LOADING,
        SUCCESS,
        RECARREGAR,
        ERROR
    }
}