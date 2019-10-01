package com.example.androidmvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.entidades.RepositorioDTO
import com.example.androidmvvm.model.interfaces.InteracaoComLista
import com.example.androidmvvm.view.recyclerViewAdapter.AdapterRepositorios
import com.example.androidmvvm.view_model.RepositorioViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RepositorioViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = getString(R.string.titulo_activity_repositorios)

        //inicialisa o recyclerView
        initRecyclerView()
        //inicialisa a viewModel
        initObservables()

        swipeRefresh_repositorios.setOnRefreshListener {
            //recarrega a primeira pagina
            viewModel.getRepositorios(true)
        }



        //Amarra o ciclo de vida do livedata ao ciclo de vida da activity
        lifecycle.addObserver(viewModel)
    }

    override fun onResume() {
        super.onResume()

    }

    private fun initObservables() {
        viewModel.repositorioData.observe(this, Observer {
            when (it.status) {//validando o status da requisição

                RepositorioDTO.STATUS.OPEN_LOADING -> {//mostra o loading
                    showLoading()
                }

                RepositorioDTO.STATUS.SUCCESS -> {
                    hideLoading()

                    if (it.recarga) {
                        recyclerViewRepositorios.adapter =
                            AdapterRepositorios(
                                context = this,
                                mValues = it.items,
                                interacaoComLista = object : InteracaoComLista<Repositorio> {
                                    override fun buscarmais() {
                                        viewModel.getRepositorios()
                                    }

                                    override fun selecionou(itemSelecionado: Repositorio) {
                                        //transição de tela
                                    }

                                })
                    } else
                        adicionarNovosItens(it.items)

                }

                RepositorioDTO.STATUS.ERROR -> {
                    hideLoading()
                    Toast.makeText(this, it.errorManseger, Toast.LENGTH_LONG).show()
                }

                RepositorioDTO.STATUS.CLOSE_LOADING -> {
                    hideLoading()
                }

                RepositorioDTO.STATUS.RECARREGAR -> {
                    hideLoading()
                }
            }
        })
    }

    private fun hideLoading() {
        swipeRefresh_repositorios.isRefreshing = false
    }

    private fun showLoading() {
        swipeRefresh_repositorios.isRefreshing = true
    }

    private fun adicionarNovosItens(items: ArrayList<Repositorio>) {
        if (recyclerViewRepositorios.adapter is AdapterRepositorios) {
            val adapter = recyclerViewRepositorios.adapter as AdapterRepositorios
            adapter.adicinarNovaLista(items)
        }
    }

    private fun initRecyclerView() {
        recyclerViewRepositorios.layoutManager = LinearLayoutManager(this)
    }
}
