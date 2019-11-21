package com.example.androidmvvm.model.entidades

import com.example.androidmvvm.model.enuns.STATUS

open class ResponseCustom(
    var errorManseger: String,
    var proximaPage: Int,
    var status: STATUS,
    var recarga: Boolean
)