package com.example.androidmvvm.view_model

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmvvm.model.entidades.Proprietario
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.repository.repository_impl.RepoDataRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response

class RepositorioViewModel : ViewModel(), LifecycleObserver {

    val repositorioData: MutableLiveData<RepositorioDTO> =
        MutableLiveData(RepositorioDTO(status = STATUS.OPEN_LOADING))

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

        repositorioData.value?.let { repoDTO ->

            repoDTO.proximaPage = if (isSwipe) 1 else repoDTO.proximaPage

            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val response = RepoDataRepository().getAll(repoDTO.proximaPage)

                    if (response.isSuccessful) {
                        val result = response.body()

                        result?.let { repo ->
                            repo.items.forEach { item ->
                                val proprietarioData = viewModelScope.async {
                                    return@async RepoDataRepository().getOwner(item.proprietario.nomeAutor)
                                }

                                val proprietario: Response<Proprietario> = proprietarioData.await()

                                if (proprietario.isSuccessful) {
                                    item.proprietario = proprietario.body() ?: item.proprietario
                                } else {
                                    proprietario.errorBody()?.let {
                                        val error = JSONObject(it.string()).get("message")
                                            .toString()

                                        Log.e("OPS", error)
                                        //dispararMensagemDeErro(error)
                                    }
                                    return@forEach
                                }
                            }

                            repositorioData.postValue(repositorioData.value?.apply {
                                status = STATUS.SUCCESS
                                recarga = isSwipe
                                proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0

                                if (isSwipe)
                                    items = repo.items
                                else
                                    items.addAll(repo.items)

                            })
                            isLoading = false
                        }
                    } else {
                        dispararMensagemDeErro(response.message())
                    }
                }
            }
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