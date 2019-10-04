package com.example.androidmvvm.view_model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.interfaces.SearchResultListener
import com.example.androidmvvm.model.retrofit.webClient.RepositorioWebClient

class RepositorioViewModel: ViewModel(), LifecycleObserver {

    private val repositorioWebClient = RepositorioWebClient()

    val repositorioData: MutableLiveData<RepositorioDTO> = MutableLiveData(RepositorioDTO(status = STATUS.OPEN_LOADING))

    private val page = 1
    private var isLoading = false

    init {
        getRepositorios(true)
    }

    fun openLoading() {
        repositorioData.postValue(repositorioData.value?.apply {
            this.status = STATUS.OPEN_LOADING
        })

    }

    fun getRepositorios(isSwipe: Boolean = false) {
        openLoading()
        isLoading = true
        if (isSwipe) {
            repositorioWebClient.getRepositorios(page, object : SearchResultListener<RepositorioDTO> {
                override fun onSearchResult(result: RepositorioDTO) {

                    result.apply {
                        status = STATUS.SUCCESS
                        recarga = isSwipe
                        proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0
                    }

                    repositorioData.value = result
                    isLoading = false
                }

                override fun onSearchErro(mensagem: String) {
                    dispararMensagemDeErro(mensagem)
                }
            })
        } else
            repositorioData.value?.let {
                repositorioWebClient.getRepositorios(it.proximaPage, object : SearchResultListener<RepositorioDTO> {
                    override fun onSearchResult(result: RepositorioDTO) {

                        repositorioData.postValue(repositorioData.value?.apply {
                            status = STATUS.SUCCESS
                            recarga = isSwipe
                            proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0

                            it.items.addAll(result.items)
                        })
                        isLoading = false
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
            status = STATUS.ERROR
        })
    }

    fun buscarMaisItens(visibleItemCount: Int, totalItemCount: Int, firstVisibleItemPosition: Int) {
        if (((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) && !isLoading
        ) {
            getRepositorios()
        }
    }

}