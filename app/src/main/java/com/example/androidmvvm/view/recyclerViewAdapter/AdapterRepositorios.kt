package com.example.androidmvvm.view.recyclerViewAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvm.R
import com.example.androidmvvm.model.entidades.Repositorio
import com.example.androidmvvm.model.interfaces.InteracaoComLista
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_repositorio.view.*

class AdapterRepositorios(
    private val context: Context,
    private val mValues: ArrayList<Repositorio>,
    private val interacaoComLista: InteracaoComLista<Repositorio>
) : RecyclerView.Adapter<AdapterRepositorios.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_repositorio,
                null
            )
        )
    }

    override fun getItemCount() = mValues.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repositorio = mValues[position]

        //TODO implementar paginação

        holder.tvNomerepositorio.text = repositorio.nomeRepositorio
        holder.tvDescricaoRepositorio.text = repositorio.descricaoRepositorio
        holder.tvUserName.text = repositorio.proprietario.nomeAutor
        holder.tvNumeroForks.text = repositorio.quantidadeDeForks.toString()
        holder.tvNumeroStars.text = repositorio.quantidadeDeEstrelas.toString()

        ContextCompat.getDrawable(context, R.mipmap.image_placeholder_350x350)?.let {
            Picasso.get()
                .load(repositorio.proprietario.avatar_url)
                .resize(50, 50)
                .centerCrop()
                .placeholder(it)
                .into(holder.imageAvatar)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvNomerepositorio: TextView = view.tvNomerepositorio
        var tvDescricaoRepositorio: TextView = view.tvDescricaoRepositorio
        var tvNumeroForks: TextView = view.tvNumeroForks
        var tvNumeroStars: TextView = view.tvNumeroStars
        var tvUserName: TextView = view.tvUserName
        var imageAvatar: ImageView = view.imageAvatar
    }
}