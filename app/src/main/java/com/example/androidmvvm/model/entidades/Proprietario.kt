package com.example.androidmvvm.model.entidades

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Proprietario(
    @SerializedName("login")
    var nomeAutor: String,
    var avatar_url: String,
    @SerializedName("url")
    var url:String,
    @SerializedName("name")
    var nomeSobrenome:String?
) : Serializable