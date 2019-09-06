package com.example.androidmvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.RepositorioDTO
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

        initRecyclerView()
        initObservables()

    }

    private fun initObservables() {
        viewModel.repositorioData.observe(this, Observer {
            when (it.status) {
                RepositorioDTO.STATUS.OPEN_LOADING -> {
                    swipeRefresh_repositorios.isRefreshing = true
                }
                RepositorioDTO.STATUS.SUCCESS -> {
                    swipeRefresh_repositorios.isRefreshing = false
                    recyclerViewRepositorios.adapter = AdapterRepositorios(mValues = it.items, context = this)
                }
                RepositorioDTO.STATUS.ERROR -> {
                    swipeRefresh_repositorios.isRefreshing = false
                    Toast.makeText(this, it.errorManseger, Toast.LENGTH_LONG).show()
                }
                RepositorioDTO.STATUS.CLOSE_LOADING -> {
                    swipeRefresh_repositorios.isRefreshing = false
                }
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewRepositorios.layoutManager = LinearLayoutManager(this)
    }
}
