package com.example.androidmvvm.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.interfaces.SearchResultListener
import com.example.androidmvvm.model.retrofit.webClient.RepositorioWebClient

class RepositorioViewModel : ViewModel() {

    private val repositorioWebClient = RepositorioWebClient()
    private val repositorioData : MutableLiveData<RepositorioDTO> = MutableLiveData()

    fun observerDataRepositorio() = repositorioData

    fun openLoading(){
        repositorioData.value = RepositorioDTO(status = RepositorioDTO.STATUS.OPEN_LOADING)
    }

    fun closeLoading(){
        repositorioData.value = RepositorioDTO(status = RepositorioDTO.STATUS.CLOSE_LOADING)
    }

    fun success(){
        closeLoading()
    }

    fun error(){
        closeLoading()
    }

    fun getRepositorios(){
        //exibir loading
        openLoading()

        repositorioWebClient.getRepositorios(0, object : SearchResultListener{
            override fun onSearchResult(result: RepositorioDTO) {
                result.status = RepositorioDTO.STATUS.CLOSE_LOADING
                repositorioData.value = result
            }

            override fun onSearchErro(mensagem: String) {
                repositorioData.value = RepositorioDTO(errorManseger = mensagem, status = RepositorioDTO.STATUS.CLOSE_LOADING)
            }
        })
    }

}