package com.example.androidmvvm.model.entidades

import com.example.androidmvvm.model.enuns.STATUS
import java.io.Serializable

class ForkDTO(
    var items: ArrayList<Fork> = arrayListOf(),
    errorManseger: String = "",
    proximaPage: Int = 1,
    status: STATUS,
    recarga: Boolean = true
) : ResponseCustom(errorManseger, proximaPage, status, recarga), Serializable