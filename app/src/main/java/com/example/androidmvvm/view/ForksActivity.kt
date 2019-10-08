package com.example.androidmvvm.view

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.util.KeyPutExtraUtil.REPOSITORIO_SELECIONADO
import com.example.androidmvvm.view.recyclerViewAdapter.AdapterForks
import com.example.androidmvvm.view_model.ForkViewModel

import kotlinx.android.synthetic.main.activity_forks.*
import kotlinx.android.synthetic.main.content_forks.*

class ForksActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ForkViewModel::class.java)
    }

    private lateinit var adapter: AdapterForks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forks)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (intent.hasExtra(REPOSITORIO_SELECIONADO)) {
            viewModel.definirRepositorio(intent.getSerializableExtra(REPOSITORIO_SELECIONADO) as Repositorio)
            initAdapter()
            initRecyclerView()
            initObservable()
        } else {
            return
        }
    }

    private fun initObservable() {
        viewModel.forkData.observe(this, Observer {
            when (it.status) {
                STATUS.SUCCESS -> {
                    hideLoading()
                    adicionarNovosItens(it.itens)
                }
                STATUS.OPEN_LOADING -> {
                    showLoading()
                }
                STATUS.CLOSE_LOADING -> {
                    hideLoading()
                }
                STATUS.ERROR -> {
                    hideLoading()
                }
                STATUS.RECARREGAR -> {

                }
            }
        })
    }

    private fun adicionarNovosItens(itens: ArrayList<Fork>) {
        adapter.mList = itens
        adapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        swipeRefresh_fork.isRefreshing = true
    }

    private fun hideLoading() {
        swipeRefresh_fork.isRefreshing = false
    }

    private fun initAdapter() {
        adapter = AdapterForks()
    }

    private fun initRecyclerView() {
        recyclerViewForks.adapter = adapter
        recyclerViewForks.layoutManager = LinearLayoutManager(this)

        val layoutManager: LinearLayoutManager = recyclerViewForks.layoutManager as LinearLayoutManager

        recyclerViewForks.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val quantidadeDeItensVisiveis = layoutManager.childCount
                val totalDeItensDaLista = layoutManager.itemCount
                val primeiraPosicaoVisivel = layoutManager.findFirstVisibleItemPosition()

                viewModel.buscarMaisItens(
                    visibleItemCount = quantidadeDeItensVisiveis,
                    totalItemCount = totalDeItensDaLista,
                    firstVisibleItemPosition = primeiraPosicaoVisivel
                )
            }
        })
    }

}
