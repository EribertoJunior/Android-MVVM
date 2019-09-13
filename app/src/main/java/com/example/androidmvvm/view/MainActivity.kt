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

    private var primeiraLista = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = getString(R.string.titulo_activity_repositorios)

        initRecyclerView()
        initObservables()
        //viewModel.getRepositorios()

        swipeRefresh_repositorios.setOnRefreshListener {

            viewModel.getRepositorios(true)
        }

        lifecycle.addObserver(viewModel)
    }

    private fun initObservables() {
        viewModel.repositorioData.observe(this, Observer {
            when (it.status) {

                RepositorioDTO.STATUS.OPEN_LOADING -> {
                    swipeRefresh_repositorios.isRefreshing = true
                }

                RepositorioDTO.STATUS.SUCCESS -> {
                    swipeRefresh_repositorios.isRefreshing = false

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
                    swipeRefresh_repositorios.isRefreshing = false
                    Toast.makeText(this, it.errorManseger, Toast.LENGTH_LONG).show()
                }

                RepositorioDTO.STATUS.CLOSE_LOADING -> {
                    swipeRefresh_repositorios.isRefreshing = false
                }

                RepositorioDTO.STATUS.RECARREGAR -> {
                    swipeRefresh_repositorios.isRefreshing = false

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
                }
            }
        })
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
