package com.example.androidmvvm.model.retrofit.webClient

import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.interfaces.SearchResultListener
import com.example.androidmvvm.model.retrofit.RetrofitConfig
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositorioWebClient {
    fun getRepositorios(page:Int, callbackResponse: SearchResultListener) {

        val call = RetrofitConfig().repositorioService().getRepositorios(page = page)

        call.enqueue(object : Callback<RepositorioDTO> {
            override fun onResponse(call: Call<RepositorioDTO>, response: Response<RepositorioDTO>) {

                response.body()?.let {
                    callbackResponse.onSearchResult(it)
                }

                response.errorBody().let {
                    if (it != null) {
                        val listaDeErros: JSONArray = JSONObject(it.string()).getJSONArray("errors")
                        for (i in 0 until listaDeErros.length()) {
                            callbackResponse.onSearchErro(listaDeErros[i] as String)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RepositorioDTO>, t: Throwable) {
                callbackResponse.onSearchErro(t.message.toString())
            }
        })
    }
}