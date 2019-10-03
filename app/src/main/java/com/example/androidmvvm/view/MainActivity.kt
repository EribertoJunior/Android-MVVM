package com.example.androidmvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var adapter: AdapterRepositorios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.title = getString(R.string.titulo_activity_repositorios)

        initAdapter()
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

    private fun initAdapter() {
        adapter =
            AdapterRepositorios(
                context = this,
                interacaoComLista = object : InteracaoComLista<Repositorio> {
                    override fun buscarmais() {
                        viewModel.getRepositorios()
                    }

                    override fun selecionou(itemSelecionado: Repositorio) {
                        //transição de tela
                    }

                })
    }

    private fun initObservables() {
        viewModel.repositorioData.observe(this, Observer {
            when (it.status) {//validando o status da requisição

                RepositorioDTO.STATUS.OPEN_LOADING -> {//mostra o loading
                    showLoading()
                }

                RepositorioDTO.STATUS.SUCCESS -> {
                    hideLoading()
                    adicionarNovosItens(it.items, it.quantidadeAdicionada, it.quantidadePorPagina)

                }

                RepositorioDTO.STATUS.ERROR -> {
                    hideLoading()
                    Toast.makeText(this, it.errorManseger, Toast.LENGTH_LONG).show()
                    adicionarNovosItens(it.items, it.quantidadeAdicionada, it.quantidadePorPagina)
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

    private fun adicionarNovosItens(
        items: ArrayList<Repositorio>,
        quantidadeAdicionada: Int,
        quantidadePorPagina: Int
    ) {

        adapter.mValues = items
        adapter.quantidadeAdicionada = quantidadeAdicionada
        adapter.quantidadePorPagina = quantidadePorPagina
        adapter.notifyDataSetChanged()

    }

    private fun initRecyclerView() {
        recyclerViewRepositorios.layoutManager = LinearLayoutManager(this)
        recyclerViewRepositorios.adapter = adapter

        val layoutManager: LinearLayoutManager =
            recyclerViewRepositorios.layoutManager as LinearLayoutManager

        recyclerViewRepositorios.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val findFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                viewModel.buscarMaisItens(visibleItemCount, totalItemCount, findFirstVisibleItemPosition)

            }
        })


    }
}
