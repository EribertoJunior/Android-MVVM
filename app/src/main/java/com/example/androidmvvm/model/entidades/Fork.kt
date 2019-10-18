package com.example.androidmvvm.model.entidades

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Fork(
    @SerializedName("user")
    var autorPR: AutorPR,
    @SerializedName("title")
    var titulo: String,
    @SerializedName("body")
    var descricao: String,
    @SerializedName("head")
    var cabecalho: Cabecalho

): Serializable