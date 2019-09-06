package com.example.androidmvvm.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.interfaces.SearchResultListener
import com.example.androidmvvm.model.retrofit.webClient.RepositorioWebClient

class RepositorioViewModel : ViewModel() {

    private val repositorioWebClient = RepositorioWebClient()
    val repositorioData : MutableLiveData<RepositorioDTO> = MutableLiveData()

    init {
        getRepositorios(0)
    }

    fun openLoading(){
        repositorioData.value = RepositorioDTO(status = RepositorioDTO.STATUS.OPEN_LOADING)
    }

    fun getRepositorios(page: Int){
        openLoading()
        repositorioWebClient.getRepositorios(page, object : SearchResultListener{
            override fun onSearchResult(result: RepositorioDTO) {
                result.status = RepositorioDTO.STATUS.SUCCESS
                repositorioData.value = result
            }

            override fun onSearchErro(mensagem: String) {
                repositorioData.value = RepositorioDTO(errorManseger = mensagem, status = RepositorioDTO.STATUS.CLOSE_LOADING)
            }
        })
    }

}