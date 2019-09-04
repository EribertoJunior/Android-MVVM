package com.example.androidmvvm.model.entidades

import java.io.Serializable

class RepositorioDTO(
    var items: ArrayList<Repositrio> = arrayListOf(),
    var errorManseger: String = "",
    var status: STATUS
) : Serializable {

    enum class STATUS {
        OPEN_LOADING,
        CLOSE_LOADING,
        SUCCESS,
        ERROR
    }
}