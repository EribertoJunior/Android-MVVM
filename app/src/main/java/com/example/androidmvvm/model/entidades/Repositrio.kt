package com.example.androidmvvm.model.entidades

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Repositrio(
    @SerializedName("name")
    var nomeRepositorio: String,
    @SerializedName("description")
    var descricaoRepositorio: String,
    @SerializedName("owner")
    var proprietario: Proprietario,
    @SerializedName("forks_count")
    var quantidadeDeEstrelas: Long,
    @SerializedName("forks")
    var quantidadeDeForks: Long,
    @SerializedName("html_url")
    var htmlSite: String
) : Serializable