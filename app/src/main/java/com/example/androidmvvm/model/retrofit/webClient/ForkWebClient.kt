package com.example.androidmvvm.model.retrofit.webClient

import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.ForkDTO
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.interfaces.SearchResultListener
import com.example.androidmvvm.model.retrofit.RetrofitConfig
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForkWebClient {
    fun getForks(
        page: Int,
        nomeProprietario: String,
        nomeRepositorio: String,
        callbackResponse: SearchResultListener<ArrayList<Fork>>
    ) {

        val call = RetrofitConfig().forkService()
            .getForks(nomeProprietario = nomeProprietario, nomeRepositorio = nomeRepositorio)

        call.enqueue(object : Callback<ArrayList<Fork>> {
            override fun onResponse(
                call: Call<ArrayList<Fork>>,
                response: Response<ArrayList<Fork>>
            ) {

                response.body()?.let {
                    callbackResponse.onSearchResult(it)
                }

                response.errorBody().let {
                    if (it != null) {
                        callbackResponse.onSearchErro(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Fork>>, t: Throwable) {
                callbackResponse.onSearchErro(t.message.toString())
            }
        })
    }
}