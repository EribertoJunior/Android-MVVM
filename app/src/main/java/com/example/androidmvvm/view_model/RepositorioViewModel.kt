package com.example.androidmvvm.view_model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.interfaces.SearchResultListener
import com.example.androidmvvm.model.retrofit.webClient.RepositorioWebClient

class RepositorioViewModel : ViewModel(), LifecycleObserver {

    private val repositorioWebClient = RepositorioWebClient()
    val repositorioData: MutableLiveData<RepositorioDTO> =
        MutableLiveData(RepositorioDTO(status = RepositorioDTO.STATUS.OPEN_LOADING))


    val page = 1
    /*init {
        getRepositorios(1)
    }*/

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
                    result.status = RepositorioDTO.STATUS.RECARREGAR

                    result.proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0

                    repositorioData.value = result

                }

                override fun onSearchErro(mensagem: String) {
                    repositorioData.value = RepositorioDTO(
                        errorManseger = mensagem,
                        status = RepositorioDTO.STATUS.CLOSE_LOADING
                    )
                }
            })
        } else

            repositorioData.value?.let {
                repositorioWebClient.getRepositorios(it.proximaPage, object : SearchResultListener {
                    override fun onSearchResult(result: RepositorioDTO) {
                        result.status = RepositorioDTO.STATUS.SUCCESS

                        result.proximaPage = repositorioData.value?.proximaPage?.plus(1) ?: 0

                        repositorioData.value = result

                    }

                    override fun onSearchErro(mensagem: String) {
                        repositorioData.value = RepositorioDTO(
                            errorManseger = mensagem,
                            status = RepositorioDTO.STATUS.CLOSE_LOADING
                        )
                    }
                })
            }
    }

}