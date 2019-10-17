package com.example.androidmvvm.view_model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.ForkDTO
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.repository.repository_impl.ForkDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForkViewModel : ViewModel(), LifecycleObserver {

    val forkData = MutableLiveData(ForkDTO(status = STATUS.OPEN_LOADING))

    private val page = 1
    private var isLoading = false

    init {
        forkData.value?.let {
            it.repositorio?.let { repositorio ->
                getForks(
                    nomeProprietario = repositorio.proprietario.nomeAutor,
                    nomeRepositorio = repositorio.nomeRepositorio
                )
            }
        }

    }

    fun openLoading() {
        forkData.postValue(forkData.value?.apply {
            this.status = STATUS.OPEN_LOADING
        })
    }

    fun getForks(nomeProprietario: String, nomeRepositorio: String, isSwipe: Boolean = false) {
        openLoading()
        isLoading = true

        forkData.value?.let {
            it.proximaPage = if (isSwipe) 1 else it.proximaPage

            viewModelScope.launch {
                withContext(Dispatchers.IO) {

                    val response = ForkDataRepository().getForks(
                        nomeProprietario = nomeProprietario,
                        nomeRepositorio = nomeRepositorio,
                        page = it.proximaPage
                    )
                    isLoading = false
                    if (response.isSuccessful) {
                        response.body()?.let { list ->
                            forkData.postValue(forkData.value?.apply {
                                status = STATUS.SUCCESS
                                recarga = isSwipe
                                proximaPage = forkData.value?.proximaPage?.plus(1) ?: 0

                                if (isSwipe)
                                    itens = list
                                else
                                    itens.addAll(list)
                            })
                        }
                    } else {
                        dispararMensagemDeErro(response.message())
                    }

                }
            }
        }


        /*if (isSwipe) {
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
        }*/
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

        if (forkData.value?.repositorio == null) {
            forkData.postValue(
                forkData.value?.apply {
                    this.repositorio = repositorio
                }
            )

            getForks(
                nomeRepositorio = repositorio.nomeRepositorio,
                nomeProprietario = repositorio.proprietario.nomeAutor
            )
        }
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
                it.repositorio?.let { repositorio ->
                    getForks(
                        nomeRepositorio = repositorio.nomeRepositorio,
                        nomeProprietario = repositorio.proprietario.nomeAutor
                    )
                }
            }
        }
    }
}