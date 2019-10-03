package com.example.androidmvvm.view_model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.interfaces.SearchResultListener
import com.example.androidmvvm.model.retrofit.webClient.RepositorioWebClient

class RepositorioViewModel : ViewModel(), LifecycleObserver {

    private val repositorioWebClient = RepositorioWebClient()
    val repositorioData: MutableLiveData<RepositorioDTO> =
        MutableLiveData(RepositorioDTO(status = RepositorioDTO.STATUS.OPEN_LOADING))

    private val page = 1

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    init {
        getRepositorios(true)
    }

    fun openLoading() {
        repositorioData.postValue(repositorioData.value?.apply {
            this.status = RepositorioDTO.STATUS.OPEN_LOADING
        })

    }

    fun getRepositorios(isSwipe: Boolean = false) {
        openLoading()

        if (isSwipe) {
            repositorioWebClient.getRepositorios(page, object : SearchResultListener {
                override fun onSearchResult(result: RepositorioDTO) {

                    result.apply {
                        status = RepositorioDTO.STATUS.SUCCESS
                        recarga = isSwipe
                        proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0
                        quantidadeAdicionada = result.items.size
                        quantidadePorPagina = result.items.size
                    }

                    repositorioData.value = result

                }

                override fun onSearchErro(mensagem: String) {
                    dispararMensagemDeErro(mensagem)
                }
            })
        } else
            repositorioData.value?.let {
                repositorioWebClient.getRepositorios(it.proximaPage, object : SearchResultListener {
                    override fun onSearchResult(result: RepositorioDTO) {

                        result.apply {
                            status = RepositorioDTO.STATUS.SUCCESS
                            recarga = isSwipe
                            proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0

                        }

                        repositorioData.postValue(repositorioData.value?.apply {
                            status = RepositorioDTO.STATUS.SUCCESS
                            recarga = isSwipe
                            proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0
                            quantidadeAdicionada = result.items.size

                            it.items.addAll(result.items)
                        })

                    }

                    override fun onSearchErro(mensagem: String) {

                        dispararMensagemDeErro(mensagem)


                    }
                })
            }
    }

    private fun dispararMensagemDeErro(mensagem: String) {
        repositorioData.postValue(repositorioData.value?.apply {
            errorManseger = mensagem
            status = RepositorioDTO.STATUS.ERROR
        })
    }

}