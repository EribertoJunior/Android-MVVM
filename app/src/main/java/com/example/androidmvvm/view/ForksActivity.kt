package com.example.androidmvvm.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.interfaces.InteracaoComLista
import com.example.androidmvvm.model.util.KeyPutExtraUtil.REPOSITORIO_SELECIONADO
import com.example.androidmvvm.view.recyclerViewAdapter.AdapterForks
import com.example.androidmvvm.view_model.ForkViewModel
import kotlinx.android.synthetic.main.activity_forks.*
import kotlinx.android.synthetic.main.content_forks.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForksActivity : AppCompatActivity() {

    private val viewModel: ForkViewModel by viewModel()

    private lateinit var adapter: AdapterForks

    private lateinit var repositorio: Repositorio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forks)
        setSupportActionBar(toolbar)


        if (intent.hasExtra(REPOSITORIO_SELECIONADO)) {
            repositorio = intent.getSerializableExtra(REPOSITORIO_SELECIONADO) as Repositorio
            viewModel.definirRepositorio(repositorio)
            initAdapter()
            initRecyclerView()
            initObservable()
        } else {
            return
        }

        swipeRefresh_fork.apply {
            setColorSchemeColors(
                ContextCompat.getColor(this@ForksActivity, R.color.colorPrimaryDark),
                ContextCompat.getColor(this@ForksActivity, R.color.colorPrimary),
                ContextCompat.getColor(this@ForksActivity, R.color.colorAccent)
            )

            setOnRefreshListener {
                viewModel.getForks(
                    nomeProprietario = repositorio.nomeRepositorio,
                    nomeRepositorio = repositorio.proprietario.nomeAutor,
                    isSwipe = true
                )
            }
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
                    Toast.makeText(this, it.errorManseger, Toast.LENGTH_LONG).show()
                    adicionarNovosItens(it.itens)
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
        adapter = AdapterForks(interacaoComLista = object : InteracaoComLista<Fork> {
            override fun selecionou(itemSelecionado: Fork) {
                startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(itemSelecionado.autorPR.urlSite)
                    }
                )
            }

        })
    }

    private fun initRecyclerView() {
        recyclerViewForks.adapter = adapter
        recyclerViewForks.layoutManager = LinearLayoutManager(this)

        val layoutManager: LinearLayoutManager =
            recyclerViewForks.layoutManager as LinearLayoutManager

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
