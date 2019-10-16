package com.example.androidmvvm.view_model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.repository_imp.RepoDataRepository
import kotlinx.coroutines.launch

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

        repositorioData.value?.let {

            viewModelScope.launch {
                val response = RepoDataRepository().getAll(it.proximaPage)

                if (response.isSuccessful) {
                    val result = response.body()

                    result?.let { repo ->
                        repositorioData.postValue(repositorioData.value?.apply {
                            status = STATUS.SUCCESS
                            recarga = isSwipe
                            proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0

                            it.items.addAll(repo.items)
                        })
                        isLoading = false
                    }
                } else {
                    dispararMensagemDeErro(response.message())
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