package com.example.androidmvvm.model.entidades

import com.example.androidmvvm.model.enuns.STATUS
import java.io.Serializable

class RepositorioDTO (
    var items: ArrayList<Repositorio> = arrayListOf(),
    errorManseger: String = "",
    proximaPage: Int = 1,
    status: STATUS,
    recarga: Boolean = true
) : ResponseCustom(errorManseger, proximaPage, status, recarga), Serializable