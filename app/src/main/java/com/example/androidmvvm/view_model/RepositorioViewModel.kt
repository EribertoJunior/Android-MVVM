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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

class RepositorioViewModel(private val repoDataRepository: RepoDataRepository) : ViewModel(),
    LifecycleObserver {

    val repositorioData: MutableLiveData<RepositorioDTO> =
        MutableLiveData(RepositorioDTO(status = STATUS.OPEN_LOADING))

    private var isLoading = false

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

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
                    val response = repoDataRepository.getAll(repoDTO.proximaPage)

                    if (response.isSuccessful) {
                        val result = response.body()

                        result?.let { repo ->

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
        if (((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) && !isLoading) {
            getRepositorios()
        }
    }

}