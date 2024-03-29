package com.example.androidmvvm.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.enuns.STATUS
import com.example.androidmvvm.model.interfaces.InteracaoComLista
import com.example.androidmvvm.model.util.KeyPutExtraUtil.REPOSITORIO_SELECIONADO
import com.example.androidmvvm.view.recyclerViewAdapter.AdapterRepositorios
import com.example.androidmvvm.view_model.RepositorioViewModel
import kotlinx.android.synthetic.main.content_repositories.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepoActivity : AppCompatActivity(), LifecycleOwner {

    private val viewModel: RepositorioViewModel by viewModel()

    private lateinit var adapter: AdapterRepositorios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)

        initAdapter()
        //inicialisa o recyclerView
        initRecyclerView()
        //inicialisa a viewModel
        initObservables()

        swipeRefresh_repositorios.apply {
            setOnRefreshListener {
                //recarrega a primeira pagina
                viewModel.getRepositorios(true)
            }

            setColorSchemeColors(
                ContextCompat.getColor(this@RepoActivity, R.color.colorPrimaryDark),
                ContextCompat.getColor(this@RepoActivity, R.color.colorPrimary),
                ContextCompat.getColor(this@RepoActivity, R.color.colorAccent)
            )
        }

        //Amarra o ciclo de vida do livedata ao ciclo de vida da activity
        lifecycle.addObserver(viewModel)
    }

    private fun initAdapter() {
        adapter =
            AdapterRepositorios(
                context = this,
                interacaoComLista = object : InteracaoComLista<Repositorio> {
                    override fun selecionou(itemSelecionado: Repositorio) {

                        startActivity(
                            Intent(this@RepoActivity, ForksActivity::class.java).apply {
                                putExtra(REPOSITORIO_SELECIONADO, itemSelecionado)
                            })
                    }
                })
    }

    private fun initObservables() {
        viewModel.repositorioData.observe(this, Observer {
            when (it.status) {//validando o status da requisição

                STATUS.OPEN_LOADING -> {//mostra o loading
                    showLoading()
                }

                STATUS.SUCCESS -> {
                    hideLoading()
                    adicionarNovosItens(it.items)
                }

                STATUS.ERROR -> {
                    hideLoading()
                    Toast.makeText(this, it.errorManseger, Toast.LENGTH_LONG).show()
                    adicionarNovosItens(it.items)
                }

                STATUS.CLOSE_LOADING -> {
                    hideLoading()
                }

                STATUS.RECARREGAR -> {
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
        items: ArrayList<Repositorio>
    ) {

        adapter.mValues = items
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

                viewModel.buscarMaisItens(
                    visibleItemCount,
                    totalItemCount,
                    findFirstVisibleItemPosition
                )
            }
        })
    }
}
