package com.example.androidmvvm.model.entidades

import com.google.gson.annotations.SerializedName

class AutorPR(
    @SerializedName("login")
    var nome: String,
    @SerializedName("avatar_url")
    var urlFoto: String,
    @SerializedName("html_url")
    var urlSite: String,
    @SerializedName("url")
    var url: String,
    var proprietario: Proprietario
)