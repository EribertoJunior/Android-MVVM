package com.example.androidmvvm.model.entidades

import com.google.gson.annotations.SerializedName

class Cabecalho(
    @SerializedName("repo")
    var repositorio: Repositorio
)