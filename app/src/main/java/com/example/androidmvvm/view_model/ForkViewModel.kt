package com.example.androidmvvm.view_model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.ForkDTO
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.interfaces.SearchResultListener
import com.example.androidmvvm.model.retrofit.webClient.ForkWebClient

class ForkViewModel : ViewModel(), LifecycleObserver {

    private val forkWebClient = ForkWebClient()

    val forkData = MutableLiveData(ForkDTO(status = STATUS.OPEN_LOADING))

    private val page = 1
    private var isLoading = false

    fun openLoading() {
        forkData.postValue(forkData.value?.apply {
            this.status = STATUS.OPEN_LOADING
        })
    }

    fun getForks(nomeProprietario: String, nomeRepositorio: String, isSwipe: Boolean = false) {
        openLoading()
        isLoading = true
        if (isSwipe) {
            forkWebClient.getForks(page = page, nomeProprietario = nomeProprietario,
                nomeRepositorio = nomeRepositorio,
                callbackResponse = object : SearchResultListener<ArrayList<Fork>> {
                    override fun onSearchResult(result: ArrayList<Fork>) {
                        atualizarForkData(result, isSwipe)
                        isLoading = false
                    }

                    override fun onSearchErro(mensagem: String) {
                        dispararMensagemDeErro(mensagem)
                    }

                })
        } else {
            forkData.value?.let {
                forkWebClient.getForks(page = it.proximaPage, nomeProprietario = nomeProprietario,
                    nomeRepositorio = nomeRepositorio,
                    callbackResponse = object : SearchResultListener<ArrayList<Fork>> {
                        override fun onSearchResult(result: ArrayList<Fork>) {
                            atualizarForkData(result, isSwipe)
                            isLoading = false
                        }

                        override fun onSearchErro(mensagem: String) {
                            dispararMensagemDeErro(mensagem)
                        }

                    })
            }
        }
    }

    private fun atualizarForkData(
        result: ArrayList<Fork>,
        isSwipe: Boolean
    ) {
        forkData.postValue(forkData.value?.apply {
            status = STATUS.SUCCESS
            recarga = isSwipe
            proximaPage = forkData.value?.proximaPage?.plus(1) ?: 0
            itens = result
        })
    }

    fun definirRepositorio(repositorio: Repositorio) {
        forkData.postValue(
            forkData.value?.apply {
                this.repositorio = repositorio
            }
        )
    }

    private fun dispararMensagemDeErro(mensagem: String) {
        forkData.postValue(
            forkData.value?.apply {
                status = STATUS.ERROR
                errorManseger = mensagem
            }
        )
    }

    fun buscarMaisItens(
        visibleItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ) {
        if (((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) && !isLoading
        ) {
            forkData.value?.let {
                it.repositorio?.let {repositorio ->
                    getForks(
                        nomeRepositorio = repositorio.nomeRepositorio,
                        nomeProprietario = repositorio.proprietario.nomeAutor
                    )
                }
            }
        }
    }
}