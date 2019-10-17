package com.example.androidmvvm.view.recyclerViewAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.Fork
import com.example.androidmvvm.model.interfaces.InteracaoComLista
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_fork.view.*

class AdapterForks(
    var mList: ArrayList<Fork> = arrayListOf(),
    var interacaoComLista: InteracaoComLista<Fork>
) : RecyclerView.Adapter<AdapterForks.ViewHolder>() {

    private val OnClickListener: View.OnClickListener = View.OnClickListener { view ->
        val fork = view.tag as Fork
        interacaoComLista.selecionou(fork)

    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_fork, null)
        )
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fork = mList[position]

        holder.apply {
            tvTituloPullRequest.text = fork.titulo
            tvDescricaoPullRequest.text = fork.descricao
            tvUserNamePR.text = fork.autorPR.nome

            Picasso.get()
                .load(fork.autorPR.urlFoto)
                .resize(40, 40)
                .centerCrop()
                .into(ivAvatarDonoPR)
        }

        with(holder.view) {
            tag = fork
            setOnClickListener(OnClickListener)
        }
    }


    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val tvTituloPullRequest: TextView = view.tvTituloPullRequest
        val tvDescricaoPullRequest: TextView = view.tvDescricaoPullRequest
        val tvUserNamePR: TextView = view.tvUserNamePR
        val tvNomeSobrenomePR: TextView = view.tvNomeSobrenomePR
        val ivAvatarDonoPR: ImageView = view.ivAvatarDonoPR
    }
}