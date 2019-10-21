package com.example.androidmvvm.view_model

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.ForkDTO
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.repository.ForkRepository
import com.example.androidmvvm.model.repository.repository_impl.ForkDataRepository
import com.example.androidmvvm.model.retrofit.api.ForkGithubApi
import com.example.androidmvvm.model.retrofit.api.ProprietarioGithubApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

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

                    val response = ForkDataRepository().getForks(nomeProprietario = nomeProprietario,nomeRepositorio = nomeRepositorio,page = it.proximaPage)

                    if (response.isSuccessful) {

                        response.body()?.let { list ->

                            list.forEach { item ->

                                val usuarioData = viewModelScope.async {
                                    return@async ForkDataRepository().getOwner(item.autorPR.nome)
                                }

                                val usuario = usuarioData.await()

                                if (usuario.isSuccessful){
                                    item.autorPR.proprietario = usuario.body() ?: item.autorPR.proprietario
                                }else{
                                    usuario.errorBody()?.let {usuarioError ->
                                        val error = JSONObject(usuarioError.string()).get("message")
                                            .toString()
                                        Log.e("OPS", error)
                                        //dispararMensagemDeErro(error)
                                    }
                                    return@forEach
                                }
                            }

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
                    isLoading = false
                }
            }
        }
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